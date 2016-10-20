package com.blackducksoftware.integration.eclipseplugin.views.providers;

import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import com.blackducksoftware.integration.eclipseplugin.common.constants.ProjectSpecificPreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.services.PreferenceStoreMock;

public class WarningContentProviderTest {

	private final String PROJECT_NAME = "project name";

	@Test
	public void testEmptyString() {
		final IPreferenceStore store = new PreferenceStoreMock();
		final WarningContentProvider provider = new WarningContentProvider(store);
		final Object[] content = provider.getElements("");
		assert (content instanceof String[]);
		assertArrayEquals(WarningContentProvider.NO_SELECTED_PROJECT, content);
	}

	@Test
	public void inputNotString() {
		final IPreferenceStore store = new PreferenceStoreMock();
		final WarningContentProvider provider = new WarningContentProvider(store);
		final Object[] content = provider.getElements(new Object());
		assert (content instanceof String[]);
		assertArrayEquals(WarningContentProvider.ERR_UNKNOWN_INPUT, content);
	}

	@Test
	public void bothActivateBooleansFalse() {
		final IPreferenceStore store = new PreferenceStoreMock();
		final WarningContentProvider provider = new WarningContentProvider(store);
		final String individualPrefName = StringUtils
				.join(new String[] { PROJECT_NAME, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
		store.setValue(PROJECT_NAME, false);
		store.setValue(individualPrefName, false);
		final Object[] content = provider.getElements(PROJECT_NAME);
		assert (content instanceof String[]);
		assertArrayEquals(WarningContentProvider.PROJECT_NOT_ACTIVATED, content);
	}

	@Test
	public void individualActivateBooleanFalse() {
		final IPreferenceStore store = new PreferenceStoreMock();
		final WarningContentProvider provider = new WarningContentProvider(store);
		final String individualPrefName = StringUtils
				.join(new String[] { PROJECT_NAME, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
		store.setValue(PROJECT_NAME, true);
		store.setValue(individualPrefName, false);
		final Object[] content = provider.getElements(PROJECT_NAME);
		assert (content instanceof String[]);
		assertArrayEquals(WarningContentProvider.PROJECT_NOT_ACTIVATED, content);
	}

	@Test
	public void groupActivateBooleanFalse() {
		final IPreferenceStore store = new PreferenceStoreMock();
		final WarningContentProvider provider = new WarningContentProvider(store);
		final String individualPrefName = StringUtils
				.join(new String[] { PROJECT_NAME, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
		store.setValue(PROJECT_NAME, false);
		store.setValue(individualPrefName, true);
		final Object[] content = provider.getElements(PROJECT_NAME);
		assert (content instanceof String[]);
		assertArrayEquals(WarningContentProvider.PROJECT_NOT_ACTIVATED, content);
	}

}
