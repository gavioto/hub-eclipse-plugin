package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.blackducksoftware.integration.eclipseplugin.common.services.PreferencesService;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
public class NewJavaProjectListenerTest {

    @Mock
    IResourceChangeEvent event;

    @Mock
    IWorkspace workspace;

    @Mock
    IResourceDelta parentDelta, childDelta;

    @Mock
    IResource nonProjectResource;

    @Mock
    IProject project;

    @Mock
    IPath path;

    @Mock
    CoreException e;

    private final String PROJECT_NAME = "project name";

    private final String OLD_PROJECT_NAME = "old project";

    private final String NEW_PROJECT_NAME = "new project";

    private void setUpForNonProjectResource() {
        Mockito.when(event.getSource()).thenReturn(workspace);
        PowerMockito.mockStatic(ResourcesPlugin.class);
        Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        Mockito.when(event.getDelta()).thenReturn(parentDelta);
        Mockito.when(parentDelta.getAffectedChildren()).thenReturn(new IResourceDelta[] { childDelta });
        Mockito.when(childDelta.getKind()).thenReturn(IResourceDelta.ADDED);
        Mockito.when(childDelta.getResource()).thenReturn(nonProjectResource);
    }

    private void setUpForAddedProject() throws CoreException {
        Mockito.when(event.getSource()).thenReturn(workspace);
        PowerMockito.mockStatic(ResourcesPlugin.class);
        Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        Mockito.when(event.getDelta()).thenReturn(parentDelta);
        Mockito.when(parentDelta.getAffectedChildren()).thenReturn(new IResourceDelta[] { childDelta });
        Mockito.when(childDelta.getKind()).thenReturn(IResourceDelta.ADDED);
        Mockito.when(childDelta.getResource()).thenReturn(project);
        Mockito.when(project.hasNature(JavaCore.NATURE_ID)).thenReturn(true);
        Mockito.when(project.getName()).thenReturn(PROJECT_NAME);
    }

    private void setUpForMovedProject() throws CoreException {
        Mockito.when(event.getSource()).thenReturn(workspace);
        PowerMockito.mockStatic(ResourcesPlugin.class);
        Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        Mockito.when(event.getDelta()).thenReturn(parentDelta);
        Mockito.when(parentDelta.getAffectedChildren()).thenReturn(new IResourceDelta[] { childDelta });
        Mockito.when(childDelta.getKind()).thenReturn(IResourceDelta.CHANGED);
        Mockito.when(childDelta.getResource()).thenReturn(project);
        Mockito.when(childDelta.getFlags()).thenReturn(IResourceDelta.MOVED_FROM);
        Mockito.when(project.hasNature(JavaCore.NATURE_ID)).thenReturn(true);
        Mockito.when(project.getName()).thenReturn(NEW_PROJECT_NAME);
        Mockito.when(childDelta.getMovedFromPath()).thenReturn(path);
        Mockito.when(path.toOSString()).thenReturn(OLD_PROJECT_NAME);
    }

    private void setUpForNonJavaProject() throws CoreException {
        Mockito.when(event.getSource()).thenReturn(workspace);
        PowerMockito.mockStatic(ResourcesPlugin.class);
        Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        Mockito.when(event.getDelta()).thenReturn(parentDelta);
        Mockito.when(parentDelta.getAffectedChildren()).thenReturn(new IResourceDelta[] { childDelta });
        Mockito.when(childDelta.getKind()).thenReturn(IResourceDelta.CHANGED);
        Mockito.when(childDelta.getResource()).thenReturn(project);
        Mockito.when(project.hasNature(JavaCore.NATURE_ID)).thenReturn(false);
    }

    private void setUpForThrowingCoreException() throws CoreException {
        Mockito.when(event.getSource()).thenReturn(workspace);
        PowerMockito.mockStatic(ResourcesPlugin.class);
        Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        Mockito.when(event.getDelta()).thenReturn(parentDelta);
        Mockito.when(parentDelta.getAffectedChildren()).thenReturn(new IResourceDelta[] { childDelta });
        Mockito.when(childDelta.getKind()).thenReturn(IResourceDelta.CHANGED);
        Mockito.when(childDelta.getResource()).thenReturn(project);
        Mockito.when(project.hasNature(JavaCore.NATURE_ID)).thenThrow(e);
    }

    private void setUpForDeltaNotAddedOrChanged() {
        Mockito.when(event.getSource()).thenReturn(workspace);
        PowerMockito.mockStatic(ResourcesPlugin.class);
        Mockito.when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        Mockito.when(event.getDelta()).thenReturn(parentDelta);
        Mockito.when(parentDelta.getAffectedChildren()).thenReturn(new IResourceDelta[] { childDelta });
        Mockito.when(childDelta.getKind()).thenReturn(IResourceDelta.REMOVED);
    }

    @Test
    public void testProjectBeingAdded() throws CoreException {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        setUpForAddedProject();
        final NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(1)).setAllProjectSpecificDefaults(PROJECT_NAME);
        Mockito.verify(information, Mockito.times(1)).addNewProject(PROJECT_NAME);
    }

    @Test
    public void testActivatedProjectBeingMoved() throws CoreException {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        Mockito.when(prefService.isActivated(OLD_PROJECT_NAME)).thenReturn(true);
        setUpForMovedProject();
        NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(1)).activateProject(NEW_PROJECT_NAME);
    }

    @Test
    public void testNonActivatedProjectBeingMoved() throws CoreException {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        Mockito.when(prefService.isActivated(OLD_PROJECT_NAME)).thenReturn(false);
        setUpForMovedProject();
        NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(0)).activateProject(NEW_PROJECT_NAME);
    }

    @Test
    public void testDeltaNotAddedOrChanged() {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        setUpForDeltaNotAddedOrChanged();
        final NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(0)).setAllProjectSpecificDefaults(PROJECT_NAME);
        Mockito.verify(information, Mockito.times(0)).addNewProject(PROJECT_NAME);
    }

    @Test
    public void testForNonJavaProject() throws CoreException {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        setUpForNonJavaProject();
        final NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(0)).setAllProjectSpecificDefaults(PROJECT_NAME);
        Mockito.verify(information, Mockito.times(0)).addNewProject(PROJECT_NAME);
    }

    @Test
    public void testWhenCoreExceptionThrown() throws CoreException {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        setUpForThrowingCoreException();
        final NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(0)).setAllProjectSpecificDefaults(PROJECT_NAME);
        Mockito.verify(information, Mockito.times(0)).addNewProject(PROJECT_NAME);
    }

    @Test
    public void testForNonProjectResource() {
        final ProjectDependencyInformation information = Mockito.mock(ProjectDependencyInformation.class);
        final PreferencesService prefService = Mockito.mock(PreferencesService.class);
        setUpForNonProjectResource();
        final NewJavaProjectListener listener = new NewJavaProjectListener(prefService, information);
        listener.resourceChanged(event);
        Mockito.verify(prefService, Mockito.times(0)).setAllProjectSpecificDefaults(PROJECT_NAME);
        Mockito.verify(information, Mockito.times(0)).addNewProject(PROJECT_NAME);
    }
}
