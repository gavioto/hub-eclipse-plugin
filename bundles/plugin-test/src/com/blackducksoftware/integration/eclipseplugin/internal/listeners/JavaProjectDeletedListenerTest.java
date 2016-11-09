package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

@RunWith(MockitoJUnitRunner.class)
public class JavaProjectDeletedListenerTest {

	@Mock
	ProjectDependencyInformation information;
	@Mock
	IResourceChangeEvent event;
	@Mock
	IResource resource;

	private final String NAME = "name";

	@Test
	public void testResourceChanged() {
		Mockito.when(event.getResource()).thenReturn(resource);
		Mockito.when(resource.getName()).thenReturn(NAME);
		final JavaProjectDeletedListener listener = new JavaProjectDeletedListener(information);
		listener.resourceChanged(event);
		Mockito.verify(information, Mockito.times(1)).removeProject(NAME);
	}

}
