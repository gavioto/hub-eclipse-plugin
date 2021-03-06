/*
 * Copyright (C) 2016 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.eclipseplugin.views.providers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;
import com.blackducksoftware.integration.eclipseplugin.startup.Activator;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.GavWithParentProject;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.InformationItemWithParentVulnerability;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.VulnerabilityWithParentGav;
import com.blackducksoftware.integration.hub.api.vulnerabilities.VulnerabilityItem;

public class ComponentTreeViewContentProvider implements ITreeContentProvider {

    private final IPreferenceStore preferenceStore;

    private final ProjectDependencyInformation projectInformation;

    private String inputProject;

    public static final String[] NO_SELECTED_PROJECT = new String[] { "No open project currently selected" };

    public static final String[] PROJECT_NOT_ACTIVATED = new String[] {
            "Black Duck scan not activated for current project" };

    public static final String[] ERR_UNKNOWN_INPUT = new String[] { "Input is of unknown type" };

    public static final String[] NO_VULNERABILITIES_TO_SHOW = new String[] { "No vulnerabilities to show!" };

    public static final String[] NO_HUB_CONNECTION = new String[] { "Cannot display vulnerabilities because you are not currently connected to the Hub" };

    public ComponentTreeViewContentProvider(IPreferenceStore preferenceStore, ProjectDependencyInformation projectInformation) {
        this.preferenceStore = preferenceStore;
        this.projectInformation = projectInformation;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof GavWithParentProject) {
            Gav gav = ((GavWithParentProject) parentElement).getGav();
            Map<Gav, List<VulnerabilityItem>> vulnMap = projectInformation.getVulnMap(inputProject);
            if (vulnMap != null) {
                if (((GavWithParentProject) parentElement).hasVulns()) {
                    List<VulnerabilityItem> vulnList = vulnMap.get(gav);
                    Iterator<VulnerabilityItem> vulnIt = vulnList.iterator();
                    VulnerabilityWithParentGav[] vulnsWithGavs = new VulnerabilityWithParentGav[vulnList.size()];
                    int i = 0;
                    while (vulnIt.hasNext()) {
                        VulnerabilityWithParentGav vulnWithGav = new VulnerabilityWithParentGav(gav, vulnIt.next());
                        vulnsWithGavs[i] = vulnWithGav;
                        i++;
                    }
                    return vulnsWithGavs;
                }
                return NO_VULNERABILITIES_TO_SHOW;
            }
        }
        if (parentElement instanceof VulnerabilityWithParentGav) {
            VulnerabilityItem vulnItem = ((VulnerabilityWithParentGav) parentElement).getVuln();
            InformationItemWithParentVulnerability baseScore = new InformationItemWithParentVulnerability(
                    "Base Score: " + Double.toString(vulnItem.getBaseScore()), vulnItem);
            InformationItemWithParentVulnerability description = new InformationItemWithParentVulnerability("Description: " + vulnItem.getDescription(),
                    vulnItem);
            InformationItemWithParentVulnerability severity = new InformationItemWithParentVulnerability("Severity: " + vulnItem.getSeverity(), vulnItem);
            return new InformationItemWithParentVulnerability[] { description, severity, baseScore };
        }
        return null;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof String) {
            String projectName = (String) inputElement;
            inputProject = projectName;
            if (projectName.equals("")) {
                return NO_SELECTED_PROJECT;
            }
            boolean isActivated = preferenceStore.getBoolean(projectName);
            if (isActivated) {
                if (Activator.getDefault().hasActiveHubConnection()) {
                    final Gav[] gavs = projectInformation.getAllDependencyGavs(projectName);
                    GavWithParentProject[] gavsWithParents = new GavWithParentProject[gavs.length];
                    for (int i = 0; i < gavs.length; i++) {
                        Gav gav = gavs[i];
                        Map<Gav, List<VulnerabilityItem>> vulnMap = projectInformation.getVulnMap(projectName);
                        boolean hasVulns = vulnMap.get(gav) != null && vulnMap.get(gav).size() > 0;
                        gavsWithParents[i] = new GavWithParentProject(gav, projectName, hasVulns);
                    }
                    return gavsWithParents;
                }
                return NO_HUB_CONNECTION;
            }
            return PROJECT_NOT_ACTIVATED;
        }
        return ERR_UNKNOWN_INPUT;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof GavWithParentProject) {
            return ((GavWithParentProject) element).getParentProject();
        }
        if (element instanceof VulnerabilityWithParentGav) {
            return ((VulnerabilityWithParentGav) element).getGav();
        }
        if (element instanceof InformationItemWithParentVulnerability) {
            return ((InformationItemWithParentVulnerability) element).getVuln();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof GavWithParentProject) {
            return true;
        }
        if (element instanceof VulnerabilityWithParentGav) {
            return true;
        }
        return false;
    }

}
