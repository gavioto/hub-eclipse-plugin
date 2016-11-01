package com.blackducksoftware.integration.eclipseplugin.common.services;

import org.eclipse.jface.preference.IPreferenceStore;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;

public class DefaultPreferencesService {

	private final IPreferenceStore prefStore;

	public DefaultPreferencesService(final IPreferenceStore prefStore) {
		this.prefStore = prefStore;
	}

	public void setDefaultConfig() {
		prefStore.setDefault(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT, "true");
	}

	public void setAllProjectSpecificDefaults(final String projectName) {
		if (prefStore.getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT).equals("true")) {
			prefStore.setDefault(projectName, true);
		} else {
			prefStore.setDefault(projectName, false);
		}
	}

}
