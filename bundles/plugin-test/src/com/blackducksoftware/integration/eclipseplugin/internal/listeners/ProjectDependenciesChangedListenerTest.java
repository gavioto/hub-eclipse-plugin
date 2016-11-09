package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JavaCore.class })
public class ProjectDependenciesChangedListenerTest {

	@Mock
	ProjectDependencyInformation information;
	@Mock
	FilePathGavExtractor extractor;
	@Mock
	DependencyInformationService depService;
	@Mock
	ElementChangedEvent e;
	@Mock
	IJavaElementDelta modelDelta, projectDelta, rootDelta;
	@Mock
	IJavaElement model, project, root;

	private void setUpTypes() {
		Mockito.when(model.getElementType()).thenReturn(IJavaElement.JAVA_MODEL);
		Mockito.when(project.getElementType()).thenReturn(IJavaElement.JAVA_PROJECT);
		Mockito.when(root.getElementType()).thenReturn(IJavaElement.PACKAGE_FRAGMENT_ROOT);
		Mockito.when(e.getDelta()).thenReturn(modelDelta);
		Mockito.when(modelDelta.getElement()).thenReturn(model);
		Mockito.when(projectDelta.getElement()).thenReturn(project);
		Mockito.when(rootDelta.getElement()).thenReturn(root);
		Mockito.when(modelDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[] { projectDelta });
		Mockito.when(projectDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[] { rootDelta });
	}

	@Test
	public void testClasspathNotChanged() {
		setUpTypes();
	}

	@Test
	public void testDependencyRemovedFromClasspath() {
		setUpTypes();
	}

	@Test
	public void testDependencyAddedToClasspath() {
		setUpTypes();
	}

	@Test
	public void testDependencyChangedOnClasspath() {
		setUpTypes();
	}

}
