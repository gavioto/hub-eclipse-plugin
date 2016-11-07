package com.blackducksoftware.integration.eclipseplugin.internal;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.ProjectDependenciesChangedListener;
import com.blackducksoftware.integration.eclipseplugin.internal.listeners.ProjectModificationListener;
import com.blackducksoftware.integration.eclipseplugin.preferences.listeners.DefaultPreferenceChangeListener;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.blackduck.integration.eclipseplugin";

	private static Activator plugin;

	private ProjectDependencyInformation information;

	public Activator() {
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		final FilePathGavExtractor extractor = new FilePathGavExtractor();
		final DependencyInformationService depService = new DependencyInformationService();
		final ProjectInformationService projService = new ProjectInformationService(depService, extractor);
		information = new ProjectDependencyInformation(projService);
		final DefaultPreferencesService defaultPrefService = new DefaultPreferencesService(
				getDefault().getPreferenceStore());
		final ProjectModificationListener projectModificationListener = new ProjectModificationListener(
				defaultPrefService, information);
		final DefaultPreferenceChangeListener defaultPrefChangeListener = new DefaultPreferenceChangeListener(
				defaultPrefService);
		final ProjectDependenciesChangedListener depsChangedListener = new ProjectDependenciesChangedListener(
				information, extractor, depService);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectModificationListener);
		getPreferenceStore().addPropertyChangeListener(defaultPrefChangeListener);
		JavaCore.addElementChangedListener(depsChangedListener);
		defaultPrefService.setDefaultConfig();
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
