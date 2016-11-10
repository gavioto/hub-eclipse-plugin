package com.blackducksoftware.integration.eclipseplugin.preferences.listeners;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;

public class DefaultPreferenceChangeListener implements IPropertyChangeListener {

	private final WorkspaceInformationService workspaceService;
	private final DefaultPreferencesService defaultPreferencesService;

	public DefaultPreferenceChangeListener(final DefaultPreferencesService defaultPreferencesService,
			final WorkspaceInformationService workspaceService) {
		this.workspaceService = workspaceService;
		this.defaultPreferencesService = defaultPreferencesService;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		final String[] projectNames = workspaceService.getJavaProjectNames();
		for (final String projectName : projectNames) {
			defaultPreferencesService.setAllProjectSpecificDefaults(projectName);
		}
	}
}
