package com.blackducksoftware.integration.eclipseplugin.common.services;

import java.io.File;

import org.eclipse.jdt.core.JavaCore;

import com.blackducksoftware.integration.eclipseplugin.common.constants.ClasspathVariables;

public class DependencyInformationService {

	public boolean isMavenDependency(final String filePath) {
		final String m2Repo = JavaCore.getClasspathVariable(ClasspathVariables.MAVEN).toString();
		final String[] m2RepoSegments = m2Repo.split(File.separator);
		final String[] filePathSegments = filePath.split(File.separator);
		if (filePathSegments.length < m2RepoSegments.length) {
			return false;
		}
		for (int i = 0; i < m2RepoSegments.length; i++) {
			if (!filePathSegments[i].equals(m2RepoSegments[i])) {
				return false;
			}
		}
		return true;
	}

	public boolean isGradleDependency(final String filePath) {
		final String[] filePathSegments = filePath.split(File.separator);
		if (filePathSegments.length < 3) {
			return false;
		}
		if (filePathSegments[filePathSegments.length - 3].equals("lib")
				|| filePathSegments[filePathSegments.length - 2].equals("plugins")
				|| filePathSegments[filePathSegments.length - 2].equals("lib")) {
			return false;
		}
		for (final String segment : filePathSegments) {
			if (segment.equals(".gradle")) {
				return true;
			}
		}
		return false;
	}
}
