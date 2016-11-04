package com.blackducksoftware.integration.eclipseplugin.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProjectDependencyInformation {

	private HashMap<String, Set<Warning>> components;

	public void addProject(final String projectName) {
		components.put(projectName, new HashSet<Warning>());
	}

	public void addWarningToProject(final String projectName, final Warning warning) {
		final Set<Warning> warningSet = components.get(projectName);
		if (warningSet != null) {
			warningSet.add(warning);
		}
	}

	public void removeProject(final String projectName) {
		components.remove(projectName);
	}

	public void removeWarningFromProject(final String projectName, final Warning warning) {
		final Set<Warning> warningSet = components.get(projectName);
		if (warningSet != null) {
			warningSet.remove(warning);
		}
	}

}
