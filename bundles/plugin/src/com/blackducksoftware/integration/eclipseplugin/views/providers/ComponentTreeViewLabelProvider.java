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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.GavWithParentProject;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.InformationItemWithParentVulnerability;
import com.blackducksoftware.integration.eclipseplugin.views.providers.utils.VulnerabilityWithParentGav;

public class ComponentTreeViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

    private Display display;

    public ComponentTreeViewLabelProvider(Display display) {
        this.display = display;
    }

    @Override
    public String getText(Object input) {
        if (input instanceof GavWithParentProject) {
            String text = "Component: " + ((GavWithParentProject) input).getGav().toString();
            return text;
        }
        if (input instanceof VulnerabilityWithParentGav) {
            String text = "Name: " + ((VulnerabilityWithParentGav) input).getVuln().getVulnerabilityName();
            return text;
        }
        if (input instanceof InformationItemWithParentVulnerability) {
            return ((InformationItemWithParentVulnerability) input).getInformationItem();
        }
        if (input instanceof String) {
            return (String) input;
        }
        return "";
    }

    @Override
    public Image getImage(Object input) {
        if (input instanceof GavWithParentProject) {
            if (((GavWithParentProject) input).hasVulns()) {
                try (InputStream image = getClass().getClassLoader().getResourceAsStream("resources/icons/rejected.gif")) {
                    return new Image(display, image);
                } catch (IOException e) {
                    return null; // don't show image
                }
            }
            try (InputStream image = getClass().getClassLoader().getResourceAsStream("resources/icons/approved.gif")) {
                return new Image(display, image);
            } catch (IOException e) {
                return null; // don't show image
            }
        }
        return null;
    }

    @Override
    public StyledString getStyledText(Object element) {
        String text = getText(element);
        return new StyledString(text);
    }

}
