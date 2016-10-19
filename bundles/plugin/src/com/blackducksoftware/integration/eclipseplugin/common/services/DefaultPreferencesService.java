package com.blackducksoftware.integration.eclipseplugin.common.services;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ProjectSpecificPreferenceNames;

public class DefaultPreferencesService {

	private final IPreferenceStore prefStore;

	public DefaultPreferencesService(final IPreferenceStore prefStore) {
		this.prefStore = prefStore;
	}

	public void setDefaultConfig() {
		prefStore.setDefault(ProjectSpecificPreferenceNames.ACTIVATE_PROJECT, "true");
		prefStore.setDefault(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT, "true");
		System.out.println(prefStore.getString(ProjectSpecificPreferenceNames.ACTIVATE_PROJECT));
		System.out.println(prefStore.getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT));
	}

	public void setAllProjectSpecificDefaults(final String projectName) {
		prefStore.setValue(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT, "true");
		final String activateProject = StringUtils
				.join(new String[] { projectName, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
		if (prefStore.getString(ProjectSpecificPreferenceNames.ACTIVATE_PROJECT).equals("true")) {
			prefStore.setDefault(activateProject, true);
		} else {
			prefStore.setDefault(activateProject, false);
		}
		if (prefStore.getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT).equals("true")) {
			prefStore.setDefault(projectName, true);
		} else {
			prefStore.setDefault(projectName, false);
		}
	}

}
