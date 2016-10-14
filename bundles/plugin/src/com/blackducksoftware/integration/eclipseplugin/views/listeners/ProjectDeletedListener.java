package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.swt.widgets.Display;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectDeletedListener implements IResourceChangeListener {

	private final WarningView warningView;
	private final Display display;

	public ProjectDeletedListener(final WarningView warningView, final Display display) {
		super();
		this.warningView = warningView;
		this.display = display;
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event != null && event.getResource() != null) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (warningView.getTable() != null) {
						warningView.setLastSelectedProjectName("");
						warningView.setTableInput("");
					}
				}
			});
		}
	}

}
