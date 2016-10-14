package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectSelectionListener implements ISelectionListener {

	private final WarningView warningView;

	public ProjectSelectionListener(final WarningView warningView) {
		super();
		this.warningView = warningView;
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection sel) {
		if (!(sel instanceof IStructuredSelection)) {
			return;
		}
		final IStructuredSelection ss = (IStructuredSelection) sel;
		final Object selectedProject = ss.getFirstElement();
		if (selectedProject instanceof IAdaptable) {
			if (((IAdaptable) selectedProject).getAdapter(IProject.class) != null) {
				if (warningView.getTable() != null) {
					final String[] pathSegments = ((IAdaptable) selectedProject).getAdapter(IProject.class).toString()
							.split("/");
					final String projectName = pathSegments[pathSegments.length - 1];
					warningView.setLastSelectedProjectName(projectName);
					warningView.setTableInput(projectName);
				}
			}
		}
	}
}
