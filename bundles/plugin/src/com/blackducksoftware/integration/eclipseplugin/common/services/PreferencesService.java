package com.blackducksoftware.integration.eclipseplugin.common.services;

import org.eclipse.jface.preference.IPreferenceStore;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;

public class PreferencesService {

    private final IPreferenceStore prefStore;

    public PreferencesService(final IPreferenceStore prefStore) {
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

    public void activateProject(String projectName) {
        prefStore.setValue(projectName, true);
    }

    public boolean isActivated(String projectName) {
        return prefStore.getBoolean(projectName);
    }
}
