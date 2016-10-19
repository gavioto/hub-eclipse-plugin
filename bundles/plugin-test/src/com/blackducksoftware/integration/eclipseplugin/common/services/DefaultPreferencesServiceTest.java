package com.blackducksoftware.integration.eclipseplugin.common.services;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ProjectSpecificPreferenceNames;

public class DefaultPreferencesServiceTest {

	private final String TEST_PROJECT_NAME = "testProject";

	@Test
	public void testSetDefaultConfig() {
		final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
		final DefaultPreferencesService service = new DefaultPreferencesService(mockPrefStore);
		service.setDefaultConfig();
		assertEquals(mockPrefStore.getString(ProjectSpecificPreferenceNames.ACTIVATE_PROJECT), "true");
		assertEquals(mockPrefStore.getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT), "true");
	}

	@Test
	public void testSetAllProjectSpecificDefaultsWithoutDefaultConfig() {
		final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
		final DefaultPreferencesService service = new DefaultPreferencesService(mockPrefStore);
		service.setAllProjectSpecificDefaults(TEST_PROJECT_NAME);
		final String prefName = StringUtils
				.join(new String[] { TEST_PROJECT_NAME, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
		assertEquals(false, mockPrefStore.getDefaultBoolean(prefName));
		assertEquals(true, mockPrefStore.getDefaultBoolean(TEST_PROJECT_NAME));
	}

	@Test
	public void testSetAllProjectSpecificDefaultsWithDefaultConfig() {
		final IPreferenceStore mockPrefStore = new PreferenceStoreMock();
		final DefaultPreferencesService service = new DefaultPreferencesService(mockPrefStore);
		service.setDefaultConfig();
		service.setAllProjectSpecificDefaults(TEST_PROJECT_NAME);
		final String prefName = StringUtils
				.join(new String[] { TEST_PROJECT_NAME, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
		assertEquals(true, mockPrefStore.getDefaultBoolean(prefName));
		assertEquals(true, mockPrefStore.getDefaultBoolean(TEST_PROJECT_NAME));
	}
}
