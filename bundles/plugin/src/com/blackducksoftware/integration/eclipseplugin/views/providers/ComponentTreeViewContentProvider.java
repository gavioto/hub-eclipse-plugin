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

    public ComponentTreeViewContentProvider(IPreferenceStore preferenceStore, ProjectDependencyInformation projectInformation) {
        this.preferenceStore = preferenceStore;
        this.projectInformation = projectInformation;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Gav) {
            Gav gav = (Gav) parentElement;
            Map<Gav, List<VulnerabilityItem>> vulnMap = projectInformation.getVulnMap(inputProject);
            if (vulnMap != null) {
                List<VulnerabilityItem> vulnList = vulnMap.get(gav);
                if (vulnList.size() == 0) {
                    return NO_VULNERABILITIES_TO_SHOW;
                }
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
        }
        if (parentElement instanceof VulnerabilityWithParentGav) {
            VulnerabilityItem vulnItem = ((VulnerabilityWithParentGav) parentElement).getVuln();
            InformationItemWithParentVulnerability baseScore = new InformationItemWithParentVulnerability(Double.toString(vulnItem.getBaseScore()), vulnItem);
            InformationItemWithParentVulnerability description = new InformationItemWithParentVulnerability(vulnItem.getDescription(), vulnItem);
            InformationItemWithParentVulnerability name = new InformationItemWithParentVulnerability(vulnItem.getVulnerabilityName(), vulnItem);
            InformationItemWithParentVulnerability severity = new InformationItemWithParentVulnerability(vulnItem.getSeverity(), vulnItem);
            return new InformationItemWithParentVulnerability[] { name, description, severity, baseScore };
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
                final Gav[] gavs = projectInformation.getAllDependencyGavs(projectName);
                return gavs;
            }
            return PROJECT_NOT_ACTIVATED;
        }
        return ERR_UNKNOWN_INPUT;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof Gav) {
            return null;
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
        if (element instanceof Gav) {
            return true;
        }
        if (element instanceof VulnerabilityWithParentGav) {
            return true;
        }
        return false;
    }

}
