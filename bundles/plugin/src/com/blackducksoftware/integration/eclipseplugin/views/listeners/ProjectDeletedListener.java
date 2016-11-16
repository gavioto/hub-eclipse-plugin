package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

import com.blackducksoftware.integration.eclipseplugin.views.ui.VulnerabilityView;

public class ProjectDeletedListener implements IResourceChangeListener {

    private final VulnerabilityView componentView;

    public ProjectDeletedListener(final VulnerabilityView componentView) {
        this.componentView = componentView;
    }

    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
        if (event != null && event.getResource() != null) {
            componentView.setLastSelectedProjectName("");
            componentView.resetInput();
        }
    }

}
