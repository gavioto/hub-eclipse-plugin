package com.blackducksoftware.integration.eclipseplugin.common.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ClasspathVariables;

public class ProjectInformationService {

	private final DependencyInformationService dependencyInformationService;

	public ProjectInformationService(final DependencyInformationService dependencyInformationService) {
		this.dependencyInformationService = dependencyInformationService;
	}

	public boolean isJavaProject(final IProject project) throws CoreException {
		return project.hasNature(JavaCore.NATURE_ID);
	}

	public String[] getMavenAndGradleDependencies(final String projectName) throws CoreException {
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
				if (dependencyInformationService.isMavenDependency(filepath)
						|| dependencyInformationService.isGradleDependency(filepath)) {
					numMavenAndGradleDeps++;
				}
			}

			// extract the gavs from the file paths of the binary dependencies
			final String[] gavs = new String[numMavenAndGradleDeps];
			int gavsIndex = 0;
			for (int j = 0; j < dependencyFilepaths.length; j++) {
				final FilePathGavExtractor extractor = new FilePathGavExtractor();
				if (dependencyInformationService.isMavenDependency(dependencyFilepaths[j])) {
					gavs[gavsIndex] = extractor.getMavenPathGav(dependencyFilepaths[j],
							JavaCore.getClasspathVariable(ClasspathVariables.MAVEN).toString()).toString();
					gavsIndex++;
				} else if (dependencyInformationService.isGradleDependency(dependencyFilepaths[j])) {
					gavs[gavsIndex] = extractor.getGradlePathGav(dependencyFilepaths[j]).toString();
					gavsIndex++;
				}
			}
			return gavs;
		} else {
			return new String[0];
		}
	}
}
