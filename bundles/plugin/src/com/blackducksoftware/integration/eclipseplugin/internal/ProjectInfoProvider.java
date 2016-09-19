package com.blackducksoftware.integration.eclipseplugin.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ProjectInfoProvider {

	private static IProject[] getAllProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}
	/*
	 * private static IJavaProject[] getJavaProjects() { final IProject[]
	 * projects = getAllProjects(); final IJavaProject[] javaProjects = new
	 * IJavaProject[getNumJavaProjects()]; int i = 0; for (final IProject
	 * project : projects) { if (isJavaProject(project)) { javaProjects[i] =
	 * JavaCore.create(project); i++; } } return javaProjects; }
	 */

	/*
	 * private static int getNumProjects() { return getAllProjects().length; }
	 */
	public static int getNumJavaProjects() {
		final IProject[] projects = getAllProjects();
		int numJava = 0;
		for (final IProject project : projects) {
			if (isJavaProject(project)) {
				numJava++;
			}
		}
		return numJava;
	}

	private static boolean isJavaProject(final IProject project) {
		try {
			return project.hasNature(JavaCore.NATURE_ID);
		} catch (final CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * private static String[] getAllProjectNames() { final IProject[] projects
	 * = getAllProjects(); final String[] names = new String[projects.length];
	 * for (int i = 0; i < projects.length; i++) { names[i] =
	 * projects[i].toString(); } return names; }
	 *
	 * private static String[] getJavaProjectNames() { final IProject[] projects
	 * = getAllProjects(); final int numJavaProjects = getNumJavaProjects();
	 * final String[] names = new String[numJavaProjects]; int javaIndex = 0;
	 * for (final IProject project : projects) { if (isJavaProject(project)) {
	 * names[javaIndex] = project.toString(); javaIndex++; } } return names; }
	 */

	public static String[][] getJavaProjectLabels() {
		final IProject[] projects = getAllProjects();
		final int numJavaProjects = getNumJavaProjects();
		final String[][] labels = new String[numJavaProjects + 1][2];
		int javaIndex = 0;
		for (final IProject project : projects) {
			if (isJavaProject(project)) {
				final String[] pathSegments = project.toString().split("/");
				labels[javaIndex][0] = labels[javaIndex][1] = pathSegments[pathSegments.length - 1];
				javaIndex++;
			}
		}
		labels[numJavaProjects][0] = "no active Java project";
		labels[numJavaProjects][1] = "NONE";
		return labels;
	}

	public static String[] getDependencies(final String projectName) {
		if (projectName.equals("")) {
			return new String[0];
		}
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (isJavaProject(project)) {
			final IJavaProject javaProject = JavaCore.create(project);
			try {
				final IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
				int numBinary = 0;
				for (final IPackageFragmentRoot root : packageFragmentRoots) {
					if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
						numBinary++;
					}
				}
				final String[] dependencies = new String[numBinary];
				int i = 0;
				for (final IPackageFragmentRoot root : packageFragmentRoots) {
					if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
						dependencies[i] = root.toString();
						i++;
					}
				}
				return dependencies;
			} catch (final JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new String[0];
			}
		} else {
			return new String[0];
		}
	}
	/*
	 * public static void printAllDependencies() { final String[]
	 * allProjectNames = getAllProjectNames(); for (final String name :
	 * allProjectNames) { final String[] pathSegments = name.split("/"); final
	 * String[] deps = getDependencies(pathSegments[pathSegments.length - 1]);
	 * for (final String dep : deps) { System.out.println(dep); } } }
	 */

	/*
	 * public static String getSelectedProject() { final IWorkbenchWindow
	 * activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); if
	 * (activeWindow != null) { final IStructuredSelection selection =
	 * (IStructuredSelection) activeWindow.getSelectionService()
	 * .getSelection(); if (selection != null && selection.getFirstElement() !=
	 * null) { final Object selected = selection.getFirstElement(); if (selected
	 * instanceof IAdaptable) { final String[] pathSegments = ((IAdaptable)
	 * selected).getAdapter(IProject.class).toString() .split("/"); return
	 * pathSegments[pathSegments.length - 1]; } else { return ""; } } else {
	 * return ""; }
	 *
	 * } else { return ""; } }
	 */
}
