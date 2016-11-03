package com.blackducksoftware.integration.eclipseplugin.internal;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
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
		final DefaultPreferencesService defaultPrefService = new DefaultPreferencesService(
				getDefault().getPreferenceStore());
		final ProjectAddedListener projectAddedListener = new ProjectAddedListener(defaultPrefService);
		final DefaultPreferenceChangeListener defaultPrefChangeListener = new DefaultPreferenceChangeListener(
				defaultPrefService);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectAddedListener);
		getPreferenceStore().addPropertyChangeListener(defaultPrefChangeListener);
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
