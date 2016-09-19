package com.blackducksoftware.integration.eclipseplugin.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.ui.part.ViewPart;

import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

public class WarningView extends ViewPart {

	private final int MAX_COLUMN_SIZE = 100;
	private TableViewer tableOfWarnings;

	// listener that changes the warnings displayed in the warning view based on
	// what project
	// is selected in the package explorer view
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

	@Override
	public void createPartControl(final Composite parent) {
		tableOfWarnings = new TableViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, tableOfWarnings);
		tableOfWarnings.setContentProvider(new WarningContentProvider());
		getSite().getPage().addSelectionListener(projectSelectionListener);

		final Table table = tableOfWarnings.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	public void createColumns(final Composite parent, final TableViewer warningTable) {
		final String[] labels = { "Component", "License", "Match Count", "Match Type", "Operational Risk",
				"Security Risk", "Usage" };
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
		if (index == 0) {
			viewCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object warning) {
					if (warning instanceof Warning) {
						return ((Warning) warning).getComponent();
					} else {
						return "";
					}
				}
			});
		} else if (index == 1) {
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
		} else if (index == 2) {
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
		} else if (index == 3) {
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
		} else if (index == 4) {
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
		} else if (index == 5) {
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
		} else if (index == 6) {
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
}
