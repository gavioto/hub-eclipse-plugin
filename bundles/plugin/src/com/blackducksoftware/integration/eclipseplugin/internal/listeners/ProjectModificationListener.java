package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

import com.blackducksoftware.integration.eclipseplugin.common.services.DefaultPreferencesService;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

public class ProjectModificationListener implements IResourceChangeListener {

	private final DefaultPreferencesService service;
	private final ProjectDependencyInformation information;

	public ProjectModificationListener(final DefaultPreferencesService service,
			final ProjectDependencyInformation information) {
		this.service = service;
		this.information = information;
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		System.out.println("I_RESOURCE_CHANGE_EVENT");
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
										if (!information.containsProject(projectName)) {
											information.addProject(projectName);
											System.out.println("PROJECT ADDED: " + projectName);
										}
									}
								} catch (final CoreException e) {
								}
							}
						} else if (delta.getKind() == IResourceDelta.REMOVED) {
							if (delta.getResource() != null) {
								final IResource resource = delta.getResource();
								try {
									if (resource instanceof IProject
											&& ((IProject) resource).hasNature(JavaCore.NATURE_ID)) {
										final String projectName = resource.getName();
										System.out.println("PROJECT REMOVED: " + projectName);
										information.removeProject(projectName);
									}
								} catch (final CoreException e) {

								}
							}
						}
					}
				}
			}
		}
		System.out.println("----------------");
		System.out.flush();
	}

}
