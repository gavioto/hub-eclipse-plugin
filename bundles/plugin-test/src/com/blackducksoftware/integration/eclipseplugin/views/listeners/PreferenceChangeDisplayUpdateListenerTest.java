package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

@RunWith(MockitoJUnitRunner.class)
public class PreferenceChangeDisplayUpdateListenerTest {

	@Mock
	WarningView warningView;
	@Mock
	PropertyChangeEvent e;
	@Mock
	TableViewer table;

	@Test
	public void testSettingTableInput() {
		Mockito.when(warningView.getTable()).thenReturn(table);
		final PreferenceChangeDisplayUpdateListener listener = new PreferenceChangeDisplayUpdateListener(warningView);
		listener.propertyChange(e);
		Mockito.verify(warningView, Mockito.times(1)).resetInput();
	}

	@Test
	public void testWhenTableNull() {
		final PreferenceChangeDisplayUpdateListener listener = new PreferenceChangeDisplayUpdateListener(warningView);
		listener.propertyChange(e);
		Mockito.verify(warningView, Mockito.times(0)).resetInput();
	}

}
