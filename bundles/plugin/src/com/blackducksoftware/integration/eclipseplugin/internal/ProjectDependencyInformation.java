package com.blackducksoftware.integration.eclipseplugin.internal;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;
import com.google.common.cache.LoadingCache;

public class ProjectDependencyInformation {

	private final LoadingCache<Gav, Warning> componentCache;
	private final HashMap<String, HashMap<Gav, Warning>> projectInfo;
	private final ProjectInformationService projService;

	private WarningView warningView;

	public ProjectDependencyInformation(final ProjectInformationService projService,
			final LoadingCache<Gav, Warning> componentCache) {
		projectInfo = new HashMap<String, HashMap<Gav, Warning>>();
		this.projService = projService;
		this.warningView = null;
		this.componentCache = componentCache;
	}

	public void setWarningView(final WarningView warningView) {
		this.warningView = warningView;
	}

	public void removeWarningView() {
		warningView = null;
	}

	public void addProject(final String projectName) {
		if (!projectInfo.containsKey(projectName)) {
			final Gav[] gavs = projService.getMavenAndGradleDependencies(projectName);
			final HashMap<Gav, Warning> deps = new HashMap<Gav, Warning>();
			for (final Gav gav : gavs) {
				try {
					deps.put(gav, componentCache.get(gav));
				} catch (final ExecutionException e) {

				}
			}
			projectInfo.put(projectName, deps);
		}
	}

	public void addWarningToProject(final String projectName, final Gav gav) {
		final HashMap<Gav, Warning> deps = projectInfo.get(projectName);
		if (deps != null) {
			try {
				deps.put(gav, componentCache.get(gav));
			} catch (final ExecutionException e) {

			}
			if (warningView != null) {
				warningView.resetInput();
			}
		}
	}

	public void removeProject(final String projectName) {
		System.out.println("REMOVING PROJECT " + projectName);
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

}
