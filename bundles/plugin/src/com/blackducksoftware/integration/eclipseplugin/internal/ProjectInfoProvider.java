package com.blackducksoftware.integration.eclipseplugin.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;

/*
 * Class that provides information about projects in the workspace
 */
public class ProjectInfoProvider {

	private static final String MAVEN_CLASSPATH_VARIABLE_NAME = "M2_REPO";

	/*
	 * Get all projects in the workspace
	 */
	private static IProject[] getAllProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}

	/*
	 * Get the number of Java projects in the workspace
	 */
	private static int getNumJavaProjects() throws CoreException {
		final IProject[] projects = getAllProjects();
		int numJava = 0;
		for (final IProject project : projects) {
			if (isJavaProject(project)) {
				numJava++;
			}
		}
		return numJava;
	}

	/*
	 * Determine whether project is a Java project
	 */
	private static boolean isJavaProject(final IProject project) throws CoreException {
		return project.hasNature(JavaCore.NATURE_ID);
	}

	/*
	 * Get the names of all Java projects in the workspace
	 */
	public static String[] getJavaProjectNames() throws CoreException {
		final IProject[] projects = getAllProjects();
		final int numJavaProjects = getNumJavaProjects();
		final String[] names = new String[numJavaProjects];
		int javaIndex = 0;
		for (final IProject project : projects) {
			if (isJavaProject(project)) {
				final String[] pathSegments = project.toString().split("/");
				final String projectName = pathSegments[pathSegments.length - 1];
				names[javaIndex] = projectName;
				javaIndex++;
			}
		}
		return names;
	}

	/*
	 * Determine whether the dependency with the given file path is a Maven
	 * dependency
	 */
	private static boolean isMavenDependency(final String filePath) {
		final String m2Repo = JavaCore.getClasspathVariable("M2_REPO").toString();
		final String[] m2RepoSegments = m2Repo.split("/");
		final String[] filePathSegments = filePath.split("/");
		if (filePathSegments.length < m2RepoSegments.length) {
			return false;
		}
		boolean isMavenDependency = true;
		for (int i = 0; i < m2RepoSegments.length; i++) {
			if (!filePathSegments[i].equals(m2RepoSegments[i])) {
				isMavenDependency = false;
				break;
			}
		}
		return isMavenDependency;
	}

	/*
	 * Determine whether the dependency with the given file path is a Gradle
	 * dependency
	 */
	private static boolean isGradleDependency(final String projectName) {
		final String[] projectFilepathSegments = projectName.split("/");
		if (projectFilepathSegments[projectFilepathSegments.length - 3].equals("lib")
				|| projectFilepathSegments[projectFilepathSegments.length - 2].equals("plugins")
				|| projectFilepathSegments[projectFilepathSegments.length - 2].equals("lib")) {
			return false;
		}
		for (final String segment : projectFilepathSegments) {
			if (segment.equals(".gradle")) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Return a string representation of each Maven and Gradle dependency
	 */
	public static String[] getMavenAndGradleDependencies(final String projectName) throws CoreException {
		if (projectName.equals("")) {
			return new String[0];
		}
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (isJavaProject(project)) {
			final IJavaProject javaProject = JavaCore.create(project);
			final IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
			int numBinary = 0;

			// extract binary dependencies (exclude source dependencies)
			for (final IPackageFragmentRoot root : packageFragmentRoots) {
				if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
					numBinary++;
				}
			}
			final String[] dependencyFilepaths = new String[numBinary];
			int i = 0;

			// get file paths of binary dependencies
			for (final IPackageFragmentRoot root : packageFragmentRoots) {
				if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
					final String fullName = root.toString();
					dependencyFilepaths[i] = fullName.split(" ")[0];
					i++;
				}
			}
			int numMavenAndGradleDeps = 0;
			for (final String filepath : dependencyFilepaths) {
				if (isMavenDependency(filepath) || isGradleDependency(filepath)) {
					numMavenAndGradleDeps++;
				}
			}

			// extract the gavs from the file paths of the binary dependencies
			final String[] gavs = new String[numMavenAndGradleDeps];
			int gavsIndex = 0;
			for (int j = 0; j < dependencyFilepaths.length; j++) {
				if (isMavenDependency(dependencyFilepaths[j])) {
					gavs[gavsIndex] = FilePathGavExtractor.getMavenPathGav(dependencyFilepaths[j],
							JavaCore.getClasspathVariable(MAVEN_CLASSPATH_VARIABLE_NAME).toString()).toString();
					gavsIndex++;
				} else if (isGradleDependency(dependencyFilepaths[j])) {
					gavs[gavsIndex] = FilePathGavExtractor.getGradlePathGav(dependencyFilepaths[j]).toString();
					gavsIndex++;
				}
			}
			return gavs;
		} else {
			return new String[0];
		}
	}

	/*
	 * Obtain the currently selected project in workbench, if a project is
	 * selected; if there is no project selected, return an empty string
	 */
	public static String getSelectedProject() {
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
