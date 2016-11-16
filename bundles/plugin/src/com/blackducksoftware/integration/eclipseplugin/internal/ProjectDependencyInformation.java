package com.blackducksoftware.integration.eclipseplugin.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.build.GavWithType;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.views.ui.VulnerabilityView;
import com.blackducksoftware.integration.hub.api.vulnerabilities.VulnerabilityItem;

public class ProjectDependencyInformation {

    private final ComponentCache componentCache;

    private final HashMap<String, ConcurrentHashMap<Gav, List<VulnerabilityItem>>> projectInfo;

    private final ProjectInformationService projService;

    private VulnerabilityView componentView;

    public ProjectDependencyInformation(final ProjectInformationService projService,
            ComponentCache componentCache) {
        projectInfo = new HashMap<>();
        this.projService = projService;
        this.componentView = null;
        this.componentCache = componentCache;
    }

    public void setComponentView(final VulnerabilityView componentView) {
        this.componentView = componentView;
    }

    public void removeComponentView() {
        componentView = null;
    }

    public void addProject(final String projectName) {
        if (!projectInfo.containsKey(projectName)) {
            reloadProject(projectName);
        }
    }

    public void reloadProject(String projectName) {
        final GavWithType[] gavs = projService.getMavenAndGradleDependencies(projectName);
        final ConcurrentHashMap<Gav, List<VulnerabilityItem>> deps = new ConcurrentHashMap<>();
        for (final GavWithType gav : gavs) {
            try {
                deps.put(gav.getGav(), componentCache.getCache().get(gav));
            } catch (final ExecutionException e) {

            }
        }
        projectInfo.put(projectName, deps);
    }

    public void addWarningToProject(final String projectName, final GavWithType gav) {
        final ConcurrentHashMap<Gav, List<VulnerabilityItem>> deps = projectInfo.get(projectName);
        if (deps != null) {
            try {
                deps.put(gav.getGav(), componentCache.getCache().get(gav));
                if (componentView != null) {
                    componentView.resetInput();
                }
            } catch (ExecutionException e) {

            }
        }
    }

    public void removeProject(final String projectName) {
        projectInfo.remove(projectName);
    }

    public void removeWarningFromProject(final String projectName, final Gav gav) {
        final ConcurrentHashMap<Gav, List<VulnerabilityItem>> dependencies = projectInfo.get(projectName);
        if (dependencies != null) {
            dependencies.remove(gav);
            if (componentView != null) {
                componentView.resetInput();
            }
        }
    }

    public boolean containsProject(final String projectName) {
        return projectInfo.containsKey(projectName);
    }

    public Gav[] getAllDependencyGavs(final String projectName) {
        final ConcurrentHashMap<Gav, List<VulnerabilityItem>> dependencyInfo = projectInfo.get(projectName);
        if (dependencyInfo != null) {
            return dependencyInfo.keySet().toArray(new Gav[dependencyInfo.keySet().size()]);
        } else {
            return new Gav[0];
        }
    }

    public GavWithVulnerabilities[] getVulns(String projectName) {
        final ConcurrentHashMap<Gav, List<VulnerabilityItem>> dependencyInfo = projectInfo.get(projectName);
        if (dependencyInfo != null) {
            Set<Gav> gavSet = dependencyInfo.keySet();
            Iterator<Gav> gavIt = gavSet.iterator();
            GavWithVulnerabilities[] allVulns = new GavWithVulnerabilities[gavSet.size()];
            int i = 0;
            while (gavIt.hasNext()) {
                Gav gav = gavIt.next();
                GavWithVulnerabilities gavWithVulns = new GavWithVulnerabilities(gav, dependencyInfo.get(gav));
                allVulns[i] = gavWithVulns;
                i++;
            }
            return allVulns;
        }
        return new GavWithVulnerabilities[0];
    }

    public Map<Gav, List<VulnerabilityItem>> getVulnMap(String projectName) {
        return projectInfo.get(projectName);
    }

}
