package com.blackducksoftware.integration.eclipseplugin.common.services;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ClasspathVariables;

public class ProjectInformationService {

	private final DependencyInformationService dependencyInformationService;
	private final FilePathGavExtractor extractor;

	public ProjectInformationService(final DependencyInformationService dependencyInformationService,
			final FilePathGavExtractor extractor) {
		this.dependencyInformationService = dependencyInformationService;
		this.extractor = extractor;
	}

	public boolean isJavaProject(final IProject project) {
		System.out.println("Java Project?");
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				System.out.println("true");
			} else {
				System.out.println("false");
			}
			return project.hasNature(JavaCore.NATURE_ID);
		} catch (final CoreException e) {
			System.out.println("issue determining");
			return false;
		}
	}

	public int getNumBinaryDependencies(final IPackageFragmentRoot[] packageFragmentRoots) {
		int numBinary = 0;
		for (final IPackageFragmentRoot root : packageFragmentRoots) {
			try {
				if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
					numBinary++;
				}
			} catch (final JavaModelException e) {
			}
		}
		return numBinary;
	}

	public String[] getBinaryDependencyFilepaths(final IPackageFragmentRoot[] packageFragmentRoots) {
		final int numBinary = getNumBinaryDependencies(packageFragmentRoots);
		final String[] dependencyFilepaths = new String[numBinary];
		int i = 0;
		for (final IPackageFragmentRoot root : packageFragmentRoots) {
			try {
				if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
					final IPath path = root.getPath();
					final String device = path.getDevice();
					String osString = path.toOSString();
					if (device != null) {
						osString = osString.replaceFirst(device, "");
					}
					dependencyFilepaths[i] = osString;
					i++;
				}
			} catch (final JavaModelException e) {
			}
		}
		return dependencyFilepaths;
	}

	public String[] getMavenAndGradleDependencies(final String projectName) {
		if (projectName.equals("")) {
			return new String[0];
		}
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project == null) {
			return new String[0];
		}
		if (isJavaProject(project)) {
			final IJavaProject javaProject = JavaCore.create(project);
			IPackageFragmentRoot[] packageFragmentRoots;
			try {
				packageFragmentRoots = javaProject.getPackageFragmentRoots();
				final String[] dependencyFilepaths = getBinaryDependencyFilepaths(packageFragmentRoots);
				return getGavsFromFilepaths(dependencyFilepaths);
			} catch (final JavaModelException e) {
				return new String[0];
			}
		} else {
			return new String[0];
		}
	}

	public int getNumMavenAndGradleDependencies(final String[] dependencyFilepaths) {
		int numDeps = 0;
		for (final String filepath : dependencyFilepaths) {
			if (dependencyInformationService.isMavenDependency(filepath)
					|| dependencyInformationService.isGradleDependency(filepath)) {
				numDeps++;
			}
		}
		return numDeps;
	}

	public String[] getGavsFromFilepaths(final String[] dependencyFilepaths) {
		final int numDeps = getNumMavenAndGradleDependencies(dependencyFilepaths);
		final String[] gavs = new String[numDeps];
		int gavsIndex = 0;
		for (int j = 0; j < dependencyFilepaths.length; j++) {
			if (dependencyInformationService.isMavenDependency(dependencyFilepaths[j])) {
				final Gav gav = extractor.getMavenPathGav(dependencyFilepaths[j],
						JavaCore.getClasspathVariable(ClasspathVariables.MAVEN).toString());

				gavs[gavsIndex] = getGavMessage(gav);
				gavsIndex++;
			} else if (dependencyInformationService.isGradleDependency(dependencyFilepaths[j])) {
				final Gav gav = extractor.getGradlePathGav(dependencyFilepaths[j]);
				gavs[gavsIndex] = getGavMessage(gav);
				gavsIndex++;
			}
		}
		return gavs;

	}

	public String getGavMessage(final Gav gav) {
		final String[] elements = new String[] { "group: " + gav.getGroupId(), "artifact: " + gav.getArtifactId(),
				"version: " + gav.getVersion() };
		return StringUtils.join(elements, ", ");
	}
}
