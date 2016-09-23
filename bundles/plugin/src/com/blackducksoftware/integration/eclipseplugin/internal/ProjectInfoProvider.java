package com.blackducksoftware.integration.eclipseplugin.internal;

import java.io.File;

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
// import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;

public class ProjectInfoProvider {

	private static final String MAVEN_CLASSPATH_VARIABLE_NAME = "M2_REPO";

	private static IProject[] getAllProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}

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

	// **************** DEAL WITH EXCEPTION BETTER **************** //
	private static boolean isJavaProject(final IProject project) throws CoreException {
		return project.hasNature(JavaCore.NATURE_ID);
	}

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

	private static boolean isMavenDependency(final String projectName) {
		final String m2Repo = JavaCore.getClasspathVariable("M2_REPO").toOSString();
		final String[] m2RepoSegments = m2Repo.split(File.separator);
		final String[] projectFilepathSegments = projectName.split(File.separator);
		if (projectFilepathSegments.length < m2RepoSegments.length) {
			return false;
		}
		boolean isMavenDependency = true;
		for (int i = 0; i < m2RepoSegments.length; i++) {
			if (!projectFilepathSegments[i].equals(m2RepoSegments[i])) {
				isMavenDependency = false;
			}
		}
		return isMavenDependency;
	}

	private static boolean isGradleDependency(final String projectName) {
		final String[] projectFilepathSegments = projectName.split(File.separator);
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

	// **************** DEAL WITH EXCEPTION BETTER **************** //
	public static String[] getMavenAndGradleDependencies(final String projectName) throws CoreException {
		if (projectName.equals("")) {
			return new String[0];
		}
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (isJavaProject(project)) {
			final IJavaProject javaProject = JavaCore.create(project);
			final IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
			int numBinary = 0;
			for (final IPackageFragmentRoot root : packageFragmentRoots) {
				if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
					numBinary++;
				}
			}
			final String[] dependencyFilepaths = new String[numBinary];
			int i = 0;
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

	public static String getSelectedProject() {
		final IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWindow != null) {
			final IStructuredSelection selection = (IStructuredSelection) activeWindow.getSelectionService()
					.getSelection();
			if (selection != null && selection.getFirstElement() != null) {
				final Object selected = selection.getFirstElement();
				if (selected instanceof IAdaptable) {
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
	}

}
