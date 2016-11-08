package com.blackducksoftware.integration.eclipseplugin.internal;

import java.util.HashMap;
import java.util.Iterator;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectDependencyInformation {

	private final HashMap<String, HashMap<Gav, Warning>> projectInfo;
	private final ProjectInformationService projService;

	private WarningView warningView;

	public ProjectDependencyInformation(final ProjectInformationService projService) {
		projectInfo = new HashMap<String, HashMap<Gav, Warning>>();
		this.projService = projService;
		this.warningView = null;
	}

	public void setWarningView(final WarningView warningView) {
		this.warningView = warningView;
	}

	public void removeWarningView() {
		warningView = null;
	}

	public void addProject(final String projectName) {
		final Gav[] gavs = projService.getMavenAndGradleDependencies(projectName);
		final HashMap<Gav, Warning> deps = new HashMap<Gav, Warning>();
		for (final Gav gav : gavs) {
			// API call to make warning
			deps.put(gav, new Warning("", 0, "", "", "", "", ""));
		}
		projectInfo.put(projectName, deps);
	}

	public void addWarningToProject(final String projectName, final Gav gav) {
		final HashMap<Gav, Warning> deps = projectInfo.get(projectName);
		if (deps != null) {
			// API call to make warning
			deps.put(gav, new Warning("", 0, "", "", "", "", ""));
			if (warningView != null) {
				warningView.resetInput();
			}
		}
	}

	public void removeProject(final String projectName) {
		projectInfo.remove(projectName);
	}

	public void removeWarningFromProject(final String projectName, final Gav gav) {
		final HashMap<Gav, Warning> dependencies = projectInfo.get(projectName);
		if (dependencies != null) {
			dependencies.remove(gav);
			if (warningView != null) {
				warningView.resetInput();
			}
		}
	}

	public boolean containsProject(final String projectName) {
		return projectInfo.containsKey(projectName);
	}

	public Gav[] getAllDependencyGavs(final String projectName) {
		final HashMap<Gav, Warning> dependencyInfo = projectInfo.get(projectName);
		if (dependencyInfo != null) {
			return dependencyInfo.keySet().toArray(new Gav[dependencyInfo.keySet().size()]);
		} else {
			return new Gav[0];
		}
	}

	public void printAllInfo() {
		final Iterator<String> nameIt = projectInfo.keySet().iterator();
		System.out.println("WORKSPACE INFO:");
		System.out.println("----------------");
		while (nameIt.hasNext()) {
			final String name = nameIt.next();
			System.out.println("PROJECT: " + name);
			final Iterator<Gav> gavIt = projectInfo.get(name).keySet().iterator();
			System.out.println("DEPENDENCIES:");
			while (gavIt.hasNext()) {
				final Gav gav = gavIt.next();
				System.out.println(gav.getGroupId() + ":" + gav.getArtifactId() + ":" + gav.getVersion());
			}
			System.out.println("----------------");
		}
	}

}
