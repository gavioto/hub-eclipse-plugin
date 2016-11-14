package com.blackducksoftware.integration.eclipseplugin.startup;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNodes;
import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.SecurePreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;
import com.blackducksoftware.integration.eclipseplugin.hub.ComponentVulnerabilityLookup;
import com.blackducksoftware.integration.eclipseplugin.internal.AuthorizationResponse;
import com.blackducksoftware.integration.eclipseplugin.internal.AuthorizationValidator;
import com.blackducksoftware.integration.eclipseplugin.internal.ComponentCache;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.JavaProjectDeletedListener;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.NewJavaProjectListener;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.ProjectDependenciesChangedListener;
import com.blackducksoftware.integration.eclipseplugin.preferences.listeners.DefaultPreferenceChangeListener;
import com.blackducksoftware.integration.hub.api.component.ComponentRestService;
import com.blackducksoftware.integration.hub.api.component.id.ComponentIdRestService;
import com.blackducksoftware.integration.hub.api.component.version.ComponentVersionRestService;
import com.blackducksoftware.integration.hub.api.vulnerabilities.VulnerabilityRestService;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.blackduck.integration.eclipseplugin";

    private final int COMPONENT_CACHE_CAPACITY = 10000;

    private static Activator plugin;

    private ProjectDependencyInformation information;

    private ComponentCache componentCache;

    private IResourceChangeListener newJavaProjectListener;

    private IResourceChangeListener javaProjectDeletedListener;

    private IPropertyChangeListener defaultPrefChangeListener;

    private IElementChangedListener depsChangedListener;

    private SecurePreferencesService securePrefService;

    private RestConnection hubConnection;

    public Activator() {
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        System.out.println("STARTING HUB ECLIPSE PLUGIN");
        plugin = this;
        final FilePathGavExtractor extractor = new FilePathGavExtractor();
        final DependencyInformationService depService = new DependencyInformationService();
        final ProjectInformationService projService = new ProjectInformationService(depService, extractor);
        final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
        securePrefService = new SecurePreferencesService(SecurePreferenceNodes.BLACK_DUCK, SecurePreferencesFactory.getDefault());
        getInitialHubConnection();
        if (hubConnection != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            ComponentRestService componentRestService = new ComponentRestService(hubConnection, gson, parser);
            ComponentVersionRestService versionRestService = new ComponentVersionRestService(hubConnection, gson, parser);
            ComponentIdRestService idRestService = new ComponentIdRestService(hubConnection, gson, parser);
            VulnerabilityRestService vulnerabilityRestService = new VulnerabilityRestService(hubConnection, gson, parser);
            ComponentVulnerabilityLookup lookup = new ComponentVulnerabilityLookup(componentRestService, versionRestService, idRestService,
                    vulnerabilityRestService);
            componentCache = new ComponentCache(lookup, COMPONENT_CACHE_CAPACITY);
        } else {
            componentCache = new ComponentCache(null, COMPONENT_CACHE_CAPACITY);
        }
        information = new ProjectDependencyInformation(projService, componentCache);
        final DefaultPreferencesService defaultPrefService = new DefaultPreferencesService(
                getDefault().getPreferenceStore());
        newJavaProjectListener = new NewJavaProjectListener(defaultPrefService, information);
        defaultPrefChangeListener = new DefaultPreferenceChangeListener(defaultPrefService, workspaceService);
        depsChangedListener = new ProjectDependenciesChangedListener(information, extractor, depService);
        javaProjectDeletedListener = new JavaProjectDeletedListener(information);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(newJavaProjectListener);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(javaProjectDeletedListener,
                IResourceChangeEvent.PRE_DELETE);
        getPreferenceStore().addPropertyChangeListener(defaultPrefChangeListener);
        JavaCore.addElementChangedListener(depsChangedListener);
        defaultPrefService.setDefaultConfig();
        addAllProjects(workspaceService);
    }

    public void addAllProjects(final WorkspaceInformationService workspaceService) {
        final String[] javaProjects = workspaceService.getJavaProjectNames();
        for (final String project : javaProjects) {
            information.addProject(project);
        }
    }

    public void reloadAllProjects(WorkspaceInformationService workspaceService) {
        final String[] javaProjects = workspaceService.getJavaProjectNames();
        for (final String project : javaProjects) {
            information.reloadProject(project);
        }
    }

    public ProjectDependencyInformation getProjectInformation() {
        return information;
    }

    public void getInitialHubConnection() {
        IPreferenceStore prefs = getDefault().getPreferenceStore();
        String hubURL = prefs.getString(PreferenceNames.HUB_URL);
        String hubUsername = prefs.getString(PreferenceNames.HUB_USERNAME);
        String hubPassword = securePrefService.getSecurePreference(SecurePreferenceNames.HUB_PASSWORD);
        String hubTimeout = prefs.getString(PreferenceNames.HUB_TIMEOUT);
        String proxyUsername = prefs.getString(PreferenceNames.PROXY_USERNAME);
        String proxyPassword = securePrefService.getSecurePreference(SecurePreferenceNames.PROXY_PASSWORD);
        String proxyPort = prefs.getString(PreferenceNames.PROXY_PORT);
        String proxyHost = prefs.getString(PreferenceNames.PROXY_HOST);
        String ignoredProxyHosts = prefs.getString(PreferenceNames.IGNORED_PROXY_HOSTS);
        HubServerConfigBuilder builder = new HubServerConfigBuilder();
        HubRestConnectionService connectionService = new HubRestConnectionService();
        AuthorizationValidator validator = new AuthorizationValidator(connectionService, builder);
        AuthorizationResponse response = validator.validateCredentials(hubUsername, hubPassword, hubURL, proxyUsername, proxyPassword, proxyPort, proxyHost,
                ignoredProxyHosts, hubTimeout);
        if (response.getConnection() != null) {
            hubConnection = response.getConnection();
        } else {
            // showAuthorizationErrorMessageBox(response.getResponseMessage());
            hubConnection = null;
        }
    }

    public void showAuthorizationErrorMessageBox(String message) {
        Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        MessageBox messageBox = new MessageBox(activeShell);
        messageBox.setMessage("When attempting to connect to Black Duck's Hub, the following error was detected. Check your authorization settings.\n"
                + message);
        messageBox.open();
    }

    public void updateHubConnection(RestConnection connection) {
        hubConnection = connection;
        if (connection != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            ComponentRestService componentRestService = new ComponentRestService(hubConnection, gson, parser);
            ComponentVersionRestService versionRestService = new ComponentVersionRestService(hubConnection, gson, parser);
            ComponentIdRestService idRestService = new ComponentIdRestService(hubConnection, gson, parser);
            VulnerabilityRestService vulnerabilityRestService = new VulnerabilityRestService(hubConnection, gson, parser);
            ComponentVulnerabilityLookup lookup = new ComponentVulnerabilityLookup(componentRestService, versionRestService, idRestService,
                    vulnerabilityRestService);
            componentCache.setVulnLookup(lookup);
            final FilePathGavExtractor extractor = new FilePathGavExtractor();
            final DependencyInformationService depService = new DependencyInformationService();
            final ProjectInformationService projService = new ProjectInformationService(depService, extractor);
            final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
            reloadAllProjects(workspaceService);
        } else {
            componentCache.setVulnLookup(null);
        }
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(newJavaProjectListener);
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(javaProjectDeletedListener);
        getPreferenceStore().removePropertyChangeListener(defaultPrefChangeListener);
        JavaCore.removeElementChangedListener(depsChangedListener);
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(final String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

}
