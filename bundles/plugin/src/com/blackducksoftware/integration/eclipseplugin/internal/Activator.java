package com.blackducksoftware.integration.eclipseplugin.internal;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.JavaProjectDeletedListener;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.NewJavaProjectListener;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.ProjectDependenciesChangedListener;
import com.blackducksoftware.integration.eclipseplugin.preferences.listeners.DefaultPreferenceChangeListener;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.blackduck.integration.eclipseplugin";

	private final int COMPONENT_CACHE_CAPACITY = 10000;

	private static Activator plugin;

	private ProjectDependencyInformation information;

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
		information = new ProjectDependencyInformation(projService, COMPONENT_CACHE_CAPACITY);
		final DefaultPreferencesService defaultPrefService = new DefaultPreferencesService(
				getDefault().getPreferenceStore());
		final NewJavaProjectListener newJavaProjectListener = new NewJavaProjectListener(defaultPrefService,
				information);
		final DefaultPreferenceChangeListener defaultPrefChangeListener = new DefaultPreferenceChangeListener(
				defaultPrefService);
		final ProjectDependenciesChangedListener depsChangedListener = new ProjectDependenciesChangedListener(
				information, extractor, depService);
		final JavaProjectDeletedListener deletedListener = new JavaProjectDeletedListener(information);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(newJavaProjectListener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(deletedListener, IResourceChangeEvent.PRE_DELETE);
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

	public ProjectDependencyInformation getProjectInformation() {
		return information;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
