package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectSelectionListener implements ISelectionListener {

	private final WarningView warningView;

	public ProjectSelectionListener(final WarningView warningView) {
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
			final IProject project = ((IAdaptable) selectedProject).getAdapter(IProject.class);
			String projectName;
			try {
				if (project != null && project.getDescription() != null && warningView.getTable() != null) {
					projectName = project.getDescription().getName();
					warningView.setLastSelectedProjectName(projectName);
					warningView.setTableInput(projectName);
				}
			} catch (final CoreException e) {
				projectName = "";
				warningView.setLastSelectedProjectName(projectName);
				warningView.setTableInput(projectName);
			}
		}
	}
}
