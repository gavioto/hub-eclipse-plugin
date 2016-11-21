package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

import com.blackducksoftware.integration.eclipseplugin.common.services.PreferencesService;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

public class NewJavaProjectListener implements IResourceChangeListener {

    private final PreferencesService service;

    private final ProjectDependencyInformation information;

    public NewJavaProjectListener(final PreferencesService service,
            final ProjectDependencyInformation information) {
        this.service = service;
        this.information = information;
    }

    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getSource() != null && event.getSource().equals(ResourcesPlugin.getWorkspace())) {
            if (event.getDelta() != null) {
                final IResourceDelta[] childrenDeltas = event.getDelta().getAffectedChildren();
                if (childrenDeltas != null) {
                    for (final IResourceDelta delta : childrenDeltas) {
                        if (delta.getKind() == IResourceDelta.ADDED || delta.getKind() == IResourceDelta.CHANGED) {
                            if (delta.getResource() != null) {
                                final IResource resource = delta.getResource();
                                try {
                                    if (resource instanceof IProject
                                            && ((IProject) resource).hasNature(JavaCore.NATURE_ID)) {
                                        final String projectName = resource.getName();
                                        service.setAllProjectSpecificDefaults(projectName);
                                        if ((delta.getFlags() | IResourceDelta.MOVED_FROM) != 0 && delta.getMovedFromPath() != null) {
                                            String[] movedFromPath = delta.getMovedFromPath().toOSString().split(File.separator);
                                            String oldProjectName = movedFromPath[movedFromPath.length - 1];
                                            information.addProject(projectName);
                                            if (service.isActivated(oldProjectName)) {
                                                service.activateProject(projectName);
                                            }
                                        } else {
                                            information.addNewProject(projectName);
                                        }
                                    }
                                } catch (final CoreException e) {
                                    /*
                                     * If error is thrown when calling hasNature(), then assume it isn't a Java
                                     * project and therefore don't do anything
                                     */
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
