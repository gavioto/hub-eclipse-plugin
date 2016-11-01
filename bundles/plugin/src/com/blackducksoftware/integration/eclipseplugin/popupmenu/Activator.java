package com.blackducksoftware.integration.eclipseplugin.popupmenu;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;
import com.blackducksoftware.integration.eclipseplugin.preferences.listeners.DefaultPreferenceChangeListener;
import com.blackducksoftware.integration.eclipseplugin.preferences.listeners.ProjectAddedListener;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.blackduck.integration.eclipseplugin";

	private static Activator plugin;

	public Activator() {
	}

	@Override
	public void start(final BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		final DependencyInformationService depService = new DependencyInformationService();
		final FilePathGavExtractor extractor = new FilePathGavExtractor();
		final ProjectInformationService projService = new ProjectInformationService(depService, extractor);
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		final DefaultPreferencesService defaultPrefService = new DefaultPreferencesService(
				getDefault().getPreferenceStore());
		final ProjectAddedListener projectAddedListener = new ProjectAddedListener(defaultPrefService);
		final DefaultPreferenceChangeListener defaultPrefChangeListener = new DefaultPreferenceChangeListener(
				defaultPrefService);

		defaultPrefService.setDefaultConfig();
		final String[] projectNames = workspaceService.getJavaProjectNames();
		for (final String projectName : projectNames) {
			defaultPrefService.setAllProjectSpecificDefaults(projectName);
		}
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectAddedListener);
		getPreferenceStore().addPropertyChangeListener(defaultPrefChangeListener);
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
