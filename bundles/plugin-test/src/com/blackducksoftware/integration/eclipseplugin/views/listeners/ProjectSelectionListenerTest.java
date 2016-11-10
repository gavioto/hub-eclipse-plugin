package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

@RunWith(MockitoJUnitRunner.class)
public class ProjectSelectionListenerTest {

	@Mock
	WarningView view;
	@Mock
	IWorkbenchPart part;
	@Mock
	IStructuredSelection structuredSel;

	private final String SELECTED_PROJECT_NAME = "name";

	private void setUpRelationships() {

	}

	@Test
	public void testNotStructuredSelection() {
		final ISelection sel = Mockito.mock(ISelection.class);
		final ProjectSelectionListener listener = new ProjectSelectionListener(view);
		listener.selectionChanged(part, sel);
		Mockito.verify(view, Mockito.times(0)).setLastSelectedProjectName(SELECTED_PROJECT_NAME);
	}

	@Test
	public void testStructuredSelection() {
		final ISelection sel = Mockito.mock(ISelection.class,
				Mockito.withSettings().extraInterfaces(IStructuredSelection.class));

	}

}
