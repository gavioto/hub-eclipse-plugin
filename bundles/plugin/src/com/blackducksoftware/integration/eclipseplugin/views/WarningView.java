package com.blackducksoftware.integration.eclipseplugin.views;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

public class WarningView extends ViewPart implements IPropertyChangeListener {

	private final int MAX_COLUMN_SIZE = 100;
	private TableViewer tableOfWarnings;

	@Override
	public void createPartControl(final Composite parent) {
		tableOfWarnings = new TableViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, tableOfWarnings);
		tableOfWarnings.setContentProvider(new WarningContentProvider());
		tableOfWarnings.setInput(Activator.getDefault().getPreferenceStore().getString("activeJavaProject"));
		final Table table = tableOfWarnings.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		;

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

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		System.out.println(event.getProperty());
		tableOfWarnings.setInput(event.getNewValue());
		tableOfWarnings.refresh();

	}
}
