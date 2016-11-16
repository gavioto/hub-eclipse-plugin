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

import org.eclipse.jface.viewers.LabelProvider;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.InformationItemWithParentVulnerability;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.VulnerabilityWithParentGav;

public class ComponentTreeViewLabelProvider extends LabelProvider {

    @Override
    public String getText(Object input) {
        if (input instanceof Gav) {
            return ((Gav) input).toString();
        }
        if (input instanceof VulnerabilityWithParentGav) {
            return ((VulnerabilityWithParentGav) input).getVuln().getVulnerabilityName();
        }
        if (input instanceof InformationItemWithParentVulnerability) {
            return ((InformationItemWithParentVulnerability) input).getInformationItem();
        }
        if (input instanceof String) {
            return (String) input;
        }
        return "";
    }

}
