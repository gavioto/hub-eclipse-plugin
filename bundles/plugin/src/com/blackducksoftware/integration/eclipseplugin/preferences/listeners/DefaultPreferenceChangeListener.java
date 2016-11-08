package com.blackducksoftware.integration.eclipseplugin.preferences.listeners;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;

public class DefaultPreferenceChangeListener implements IPropertyChangeListener {

	private final DependencyInformationService depService;
	private final FilePathGavExtractor extractor;
	private final ProjectInformationService projService;
	private final WorkspaceInformationService workspaceService;
	private final DefaultPreferencesService defaultPreferencesService;

	public DefaultPreferenceChangeListener(final DefaultPreferencesService defaultPreferencesService) {
		this.depService = new DependencyInformationService();
		this.extractor = new FilePathGavExtractor();
		this.projService = new ProjectInformationService(depService, extractor);
		this.workspaceService = new WorkspaceInformationService(projService);
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
