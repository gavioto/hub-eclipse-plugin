package com.blackducksoftware.integration.eclipseplugin.preferences.listeners;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPreferenceChangeListenerTest {

	@Mock
	WorkspaceInformationService workspaceService;
	@Mock
	DefaultPreferencesService prefService;
	@Mock
	PropertyChangeEvent e;

	private final String[] PROJECT_NAMES = { "project 1", "project 2", "project 3", "project 4" };

	@Test
	public void testThatProjectDefaultsSet() {
		Mockito.when(workspaceService.getJavaProjectNames()).thenReturn(PROJECT_NAMES);
		final DefaultPreferenceChangeListener listener = new DefaultPreferenceChangeListener(prefService,
				workspaceService);
		listener.propertyChange(e);
		Mockito.verify(prefService, Mockito.times(1)).setAllProjectSpecificDefaults(PROJECT_NAMES[0]);
		Mockito.verify(prefService, Mockito.times(1)).setAllProjectSpecificDefaults(PROJECT_NAMES[1]);
		Mockito.verify(prefService, Mockito.times(1)).setAllProjectSpecificDefaults(PROJECT_NAMES[2]);
		Mockito.verify(prefService, Mockito.times(1)).setAllProjectSpecificDefaults(PROJECT_NAMES[3]);
	}
}
