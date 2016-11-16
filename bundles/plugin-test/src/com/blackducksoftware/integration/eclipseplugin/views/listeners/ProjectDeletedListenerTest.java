package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.views.ui.VulnerabilityView;

@RunWith(MockitoJUnitRunner.class)
public class ProjectDeletedListenerTest {

    @Mock
    IResourceChangeEvent e;

    @Mock
    VulnerabilityView view;

    @Mock
    IResource resource;

    @Test
    public void testWhenProjectDeleted() {
        Mockito.when(e.getResource()).thenReturn(resource);
        final ProjectDeletedListener listener = new ProjectDeletedListener(view);
        listener.resourceChanged(e);
        Mockito.verify(view, Mockito.times(1)).setLastSelectedProjectName("");
        Mockito.verify(view, Mockito.times(1)).resetInput();
    }

}
