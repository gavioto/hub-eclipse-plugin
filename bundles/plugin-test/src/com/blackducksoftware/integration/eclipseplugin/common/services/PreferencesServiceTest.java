package com.blackducksoftware.integration.eclipseplugin.common.services;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;

public class PreferencesServiceTest {

    private final String testProject = "testProject";

    @Test
    public void testSetDefaultConfig() {
        final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
        final PreferencesService service = new PreferencesService(mockPrefStore);
        service.setDefaultConfig();
        assertEquals("Default behavior is not to activate Black Duck scan by default", mockPrefStore.getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT),
                "true");
    }

    @Test
    public void testIndividualProjectDefaultSettings() {
        final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
        final PreferencesService service = new PreferencesService(mockPrefStore);
        service.setDefaultConfig();
        service.setAllProjectSpecificDefaults(testProject);
        assertEquals("Black Duck scan not activated for new Java project by default", mockPrefStore.getBoolean(testProject), true);
    }

    @Test
    public void testIndividualProjectDefaultSettingsWhenActivateByDefaultFalse() {
        final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
        final PreferencesService service = new PreferencesService(mockPrefStore);
        service.setDefaultConfig();
        mockPrefStore.setValue(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT, "false");
        service.setAllProjectSpecificDefaults(testProject);
        assertEquals("Scan automatically activated for new Java project even though default behavior is not to activate scan",
                mockPrefStore.getBoolean(testProject), false);
    }
}
