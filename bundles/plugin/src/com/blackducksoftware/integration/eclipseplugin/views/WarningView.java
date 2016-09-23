package com.blackducksoftware.integration.eclipseplugin.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectInfoProvider;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

public class WarningView extends ViewPart {

	private final int MAX_COLUMN_SIZE = 100;
	private TableViewer tableOfWarnings;

	// listener that changes the warnings displayed in the warning view based on
	// what project is selected in the package explorer view
	private final ISelectionListener projectSelectionListener = new ISelectionListener() {
		@Override
		public void selectionChanged(final IWorkbenchPart part, final ISelection sel) {
			if (!(sel instanceof IStructuredSelection)) {
				return;
			}
			final IStructuredSelection ss = (IStructuredSelection) sel;
			final Object selectedProject = ss.getFirstElement();
			if (selectedProject instanceof IAdaptable) {
				final String[] pathSegments = ((IAdaptable) selectedProject).getAdapter(IProject.class).toString()
						.split("/");
				final String projectName = pathSegments[pathSegments.length - 1];
				tableOfWarnings.setInput(projectName);
			}
		}
	};

	// Make warning view display "no project currently selected" if project is
	// deleted (because in order to delete a project you must first select it)
	private final IResourceChangeListener projectDeletedListener = new IResourceChangeListener() {

		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			// TODO Auto-generated method stub
			if (event != null && event.getResource() != null) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						tableOfWarnings.setInput("");
					}
				});
			}
		}
	};

	private final IPropertyChangeListener projectSelectedListener = new IPropertyChangeListener() {

		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			tableOfWarnings.setInput(ProjectInfoProvider.getSelectedProject());
		}

	};

	@Override
	public void createPartControl(final Composite parent) {
		tableOfWarnings = new TableViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableOfWarnings.setContentProvider(new WarningContentProvider());
		createColumns(parent, tableOfWarnings);
		tableOfWarnings.setInput("");
		getSite().getPage().addSelectionListener(projectSelectionListener);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(projectSelectedListener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectDeletedListener,
				IResourceChangeEvent.PRE_DELETE);

		final Table table = tableOfWarnings.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	private void createColumns(final Composite parent, final TableViewer warningTable) {
		final String[] labels = { "Component", "Match Count", "Match Type", "Usage", "License", "Security Risk",
				"Operational Risk", };
		final int[] columnBounds = new int[labels.length];
		for (int i = 0; i < columnBounds.length; i++) {
			columnBounds[i] = MAX_COLUMN_SIZE;
		}
		for (int i = 0; i < labels.length; i++) {
			final TableViewerColumn viewCol = new TableViewerColumn(warningTable, SWT.NONE);
			final TableColumn col = viewCol.getColumn();
			col.setText(labels[i]);
			col.setWidth(columnBounds[i]);
			col.setResizable(true);
			col.setMoveable(true);
			setLabel(viewCol, i);
		}
	}

	public void setLabel(final TableViewerColumn viewCol, final int index) {
		if (index == Warning.COMPONENT_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getComponent();
					} else if (warning instanceof String) {
						return (String) warning;
					} else {
						return "unknown input type";
					}
				}
			});
		} else if (index == Warning.LICENSE_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getLicense();
					} else {
						return "";
					}
				}
			});
		} else if (index == Warning.MATCH_COUNT_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getMatchCount();
					} else {
						return "";
					}
				}
			});
		} else if (index == Warning.MATCH_TYPE_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getMatchType();
					} else {
						return "";
					}
				}
			});
		} else if (index == Warning.OPERATIONAL_RISK_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getOperationalRisk();
					} else {
						return "";
					}
				}
			});
		} else if (index == Warning.SECURITY_RISK_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getSecurityRisk();
					} else {
						return "";
					}
				}
			});
		} else if (index == Warning.USAGE_COLUMN_INDEX) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getUsage();
					} else {
						return "";
					}
				}
			});
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		tableOfWarnings.getControl().setFocus();
	}

	@Override
	public void dispose() {
		super.dispose();
		getSite().getPage().removeSelectionListener(projectSelectionListener);
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(projectSelectedListener);
	}
}
