package com.blackducksoftware.integration.eclipseplugin.common.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResourcesPlugin.class, PlatformUI.class })
public class WorkspaceInformationServiceTest {

	@Mock
	ProjectInformationService projService;
	@Mock
	IProject nonJavaProject;
	@Mock
	IProjectDescription nonJavaProjectDescription;
	@Mock
	IProject javaProject;
	@Mock
	IProjectDescription javaProjectDescription;
	@Mock
	IProject projectThatThrowsException;
	@Mock
	IWorkspace workspace;
	@Mock
	IWorkspaceRoot workspaceRoot;
	@Mock
	IWorkbench workbench;
	@Mock
	IWorkbenchWindow window;
	@Mock
	ISelectionService selectionService;
	@Mock
	IStructuredSelection selection;
	@Mock
	IAdaptable adaptableSelection;
	@Mock
	CoreException coreException;

	private final String NON_JAVA_PROJECT_NAME = "non-java-project-name";
	private final String JAVA_PROJECT_NAME = "java-project-name";

	private void setUpWorkspace() {
		PowerMockito.mockStatic(ResourcesPlugin.class);
		PowerMockito.mockStatic(PlatformUI.class);
		Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
		Mockito.when(workspace.getRoot()).thenReturn(workspaceRoot);
		Mockito.when(workspaceRoot.getProjects()).thenReturn(new IProject[] { nonJavaProject, javaProject });
		Mockito.when(PlatformUI.getWorkbench()).thenReturn(workbench);
	}

	@Test
	public void testGettingSelectedProject() throws CoreException {
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		setUpWorkspace();
		prepareProjectsAndDescriptions();
		Mockito.when(workbench.getActiveWorkbenchWindow()).thenReturn(window);
		Mockito.when(window.getSelectionService()).thenReturn(selectionService);
		Mockito.when(selectionService.getSelection()).thenReturn(selection);
		Mockito.when(selection.getFirstElement()).thenReturn(adaptableSelection);
		Mockito.when(adaptableSelection.getAdapter(IProject.class)).thenReturn(javaProject);
		final String selectedProject = workspaceService.getSelectedProject();
		assertEquals(JAVA_PROJECT_NAME, selectedProject);
	}

	@Test
	public void testGettingSelectedProjectWhenCoreExceptionThrown() throws CoreException {
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		setUpWorkspace();
		prepareProjectsAndDescriptions();
		Mockito.when(workbench.getActiveWorkbenchWindow()).thenReturn(window);
		Mockito.when(window.getSelectionService()).thenReturn(selectionService);
		Mockito.when(selectionService.getSelection()).thenReturn(selection);
		Mockito.when(selection.getFirstElement()).thenReturn(adaptableSelection);
		Mockito.when(adaptableSelection.getAdapter(IProject.class)).thenReturn(projectThatThrowsException);
		final String selectedProject = workspaceService.getSelectedProject();
		assertEquals("", selectedProject);
	}

	@Test
	public void testSelectedProjectWhenWindowNull() {
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		setUpWorkspace();
		Mockito.when(workbench.getActiveWorkbenchWindow()).thenReturn(null);
		final String selectedProject = workspaceService.getSelectedProject();
		assertEquals("", selectedProject);
	}

	@Test
	public void testGettingProjectsFunctionality() {
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		setUpWorkspace();
		final IProject[] projects = workspaceService.getAllProjects();
		assertArrayEquals(new IProject[] { nonJavaProject, javaProject }, projects);
	}

	private void prepareProjectsAndDescriptions() throws CoreException {
		Mockito.when(projService.isJavaProject(nonJavaProject)).thenReturn(false);
		Mockito.when(projService.isJavaProject(javaProject)).thenReturn(true);
		Mockito.when(nonJavaProject.getDescription()).thenReturn(nonJavaProjectDescription);
		Mockito.when(javaProject.getDescription()).thenReturn(javaProjectDescription);
		Mockito.when(nonJavaProjectDescription.getName()).thenReturn(NON_JAVA_PROJECT_NAME);
		Mockito.when(javaProjectDescription.getName()).thenReturn(JAVA_PROJECT_NAME);
		Mockito.when(projectThatThrowsException.getDescription()).thenThrow(coreException);
	}

	@Test
	public void testGetNumJavaProjects() throws CoreException {
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		setUpWorkspace();
		prepareProjectsAndDescriptions();
		final int numProjects = workspaceService.getNumJavaProjects();
		assertEquals(1, numProjects);
	}

	@Test
	public void testGetJavaProjectNames() throws CoreException {
		final WorkspaceInformationService workspaceService = new WorkspaceInformationService(projService);
		setUpWorkspace();
		prepareProjectsAndDescriptions();
		final String[] javaProjectNames = workspaceService.getJavaProjectNames();
		assertEquals(1, javaProjectNames.length);
		assertEquals(JAVA_PROJECT_NAME, javaProjectNames[0]);
	}

}
