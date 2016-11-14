package com.blackducksoftware.integration.eclipseplugin.internal;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectDependencyInformation {

    private final ComponentCache componentCache;

    private final HashMap<String, ConcurrentHashMap<Gav, List<Vulnerability>>> projectInfo;

    private final ProjectInformationService projService;

    private WarningView warningView;

    public ProjectDependencyInformation(final ProjectInformationService projService,
            ComponentCache componentCache) {
        projectInfo = new HashMap<>();
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
            reloadProject(projectName);
        }
    }

    public void reloadProject(String projectName) {
        final Gav[] gavs = projService.getMavenAndGradleDependencies(projectName);
        final ConcurrentHashMap<Gav, List<Vulnerability>> deps = new ConcurrentHashMap<>();
        for (final Gav gav : gavs) {
            try {
                deps.put(gav, componentCache.getCache().get(gav));
            } catch (final ExecutionException e) {

            }
        }
        projectInfo.put(projectName, deps);
    }

    public void addWarningToProject(final String projectName, final Gav gav) {
        final ConcurrentHashMap<Gav, List<Vulnerability>> deps = projectInfo.get(projectName);
        if (deps != null) {
            try {
                deps.put(gav, componentCache.getCache().get(gav));
                System.out.println("INSERTING COMPONENT " + gav + " INTO PROJECT " + projectName);
            } catch (ExecutionException e) {

            }
        }
    }

    public void removeProject(final String projectName) {
        projectInfo.remove(projectName);
    }

    public void removeWarningFromProject(final String projectName, final Gav gav) {
        final ConcurrentHashMap<Gav, List<Vulnerability>> dependencies = projectInfo.get(projectName);
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
        final ConcurrentHashMap<Gav, List<Vulnerability>> dependencyInfo = projectInfo.get(projectName);
        if (dependencyInfo != null) {
            return dependencyInfo.keySet().toArray(new Gav[dependencyInfo.keySet().size()]);
        } else {
            return new Gav[0];
        }
    }

}
