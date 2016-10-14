package com.blackducksoftware.integration.eclipseplugin.common.utils;

import org.eclipse.jdt.core.JavaCore;

import com.blackducksoftware.integration.eclipseplugin.common.constants.ClasspathVariables;

public class DependencyInformationService {

	public boolean isMavenDependency(final String filePath) {
		final String m2Repo = JavaCore.getClasspathVariable(ClasspathVariables.MAVEN).toString();
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

	public boolean isGradleDependency(final String projectName) {
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
}
