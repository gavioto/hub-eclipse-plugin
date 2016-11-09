package com.blackducksoftware.integration.eclipseplugin.common.services;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;

public class DefaultPreferencesServiceTest {

	private final String testProject = "testProject";

	@Test
	public void testSetDefaultConfig() {
		final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
		final DefaultPreferencesService service = new DefaultPreferencesService(mockPrefStore);
		service.setDefaultConfig();
		assertEquals(mockPrefStore.getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT), "true");
	}

	@Test
	public void testIndividualProjectDefaultSettings() {
		final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
		final DefaultPreferencesService service = new DefaultPreferencesService(mockPrefStore);
		service.setDefaultConfig();
		service.setAllProjectSpecificDefaults(testProject);
		assertEquals(mockPrefStore.getBoolean(testProject), true);
	}

	@Test
	public void testIndividualProjectDefaultSettingsWhenActivateByDefaultFalse() {
		final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
		final DefaultPreferencesService service = new DefaultPreferencesService(mockPrefStore);
		service.setDefaultConfig();
		mockPrefStore.setValue(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT, "false");
		service.setAllProjectSpecificDefaults(testProject);
		assertEquals(mockPrefStore.getBoolean(testProject), false);
	}
}
