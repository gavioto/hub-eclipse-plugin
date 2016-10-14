package com.blackducksoftware.integration.eclipseplugin.common.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WorkspaceInformationService {

	private final ProjectInformationService projectInformationService;

	public WorkspaceInformationService(final ProjectInformationService projectInformationService) {
		this.projectInformationService = projectInformationService;
	}

	public IProject[] getAllProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}

	public int getNumJavaProjects() throws CoreException {
		final IProject[] projects = getAllProjects();
		int numJava = 0;
		for (final IProject project : projects) {
			if (projectInformationService.isJavaProject(project)) {
				numJava++;
			}
		}
		return numJava;
	}

	public String[] getJavaProjectNames() throws CoreException {
		final IProject[] projects = getAllProjects();
		final int numJavaProjects = getNumJavaProjects();
		final String[] names = new String[numJavaProjects];
		int javaIndex = 0;
		for (final IProject project : projects) {
			if (projectInformationService.isJavaProject(project)) {
				final String[] pathSegments = project.toString().split("/");
				final String projectName = pathSegments[pathSegments.length - 1];
				names[javaIndex] = projectName;
				javaIndex++;
			}
		}
		return names;
	}

	public String getSelectedProject() {
		final IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWindow != null) {
			final IStructuredSelection selection = (IStructuredSelection) activeWindow.getSelectionService()
					.getSelection();
			if (selection != null && selection.getFirstElement() != null) {
				final Object selected = selection.getFirstElement();
				if (selected != null && selected instanceof IAdaptable) {
					if (((IAdaptable) selected).getAdapter(IProject.class) != null) {
						final String[] pathSegments = ((IAdaptable) selected).getAdapter(IProject.class).toString()
								.split("/");
						return pathSegments[pathSegments.length - 1];
					} else {
						return "";
					}

				} else {
					return "";
				}
			} else {
				return "";
			}

		} else {
			return "";
		}
	}

}
