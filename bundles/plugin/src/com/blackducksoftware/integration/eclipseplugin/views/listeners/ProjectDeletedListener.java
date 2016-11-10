package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectDeletedListener implements IResourceChangeListener {

	private final WarningView warningView;

	public ProjectDeletedListener(final WarningView warningView) {
		this.warningView = warningView;
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event != null && event.getResource() != null) {
			warningView.setLastSelectedProjectName("");
			warningView.resetInput();
		}
	}

}
