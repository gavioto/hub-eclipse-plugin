package com.blackducksoftware.integration.eclipseplugin.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;
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

/*
 * Class that implements the warning view UI
 */
public class WarningView extends ViewPart {

	private final int MAX_COLUMN_SIZE = 100;
	private TableViewer tableOfWarnings;
	private String lastSelectedProjectName = "";

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
				if (((IAdaptable) selectedProject).getAdapter(IProject.class) != null) {
					final String[] pathSegments = ((IAdaptable) selectedProject).getAdapter(IProject.class).toString()
							.split("/");
					final String projectName = pathSegments[pathSegments.length - 1];
					lastSelectedProjectName = projectName;
					tableOfWarnings.setInput(lastSelectedProjectName);
				}

			}
		}
	};

	// Make warning view display "no project currently selected" if project is
	// deleted (because in order to delete a project you must first select it)
	private final IResourceChangeListener projectDeletedListener = new IResourceChangeListener() {

		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			if (event != null && event.getResource() != null) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						lastSelectedProjectName = "";
						tableOfWarnings.setInput(lastSelectedProjectName);
					}
				});
			}
		}
	};

	// listener that updates view accordingly when any project's dependencies
	// change
	private final IElementChangedListener dependenciesChangedListener = new IElementChangedListener() {

		@Override
		public void elementChanged(final ElementChangedEvent event) {
			visit(event.getDelta());
		}

		private void visit(final IJavaElementDelta delta) {
			final IJavaElement el = delta.getElement();
			switch (el.getElementType()) {
			case IJavaElement.JAVA_MODEL:
				visitChildren(delta);
				break;
			case IJavaElement.JAVA_PROJECT:
				if (isClasspathChanged(delta.getFlags())) {
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							tableOfWarnings.setInput(lastSelectedProjectName);
						}
					});
				}
				break;
			default:
				break;
			}
		}

		private boolean isClasspathChanged(final int flags) {
			return 0 != (flags
					& (IJavaElementDelta.F_CLASSPATH_CHANGED | IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED));
		}

		public void visitChildren(final IJavaElementDelta delta) {
			for (final IJavaElementDelta c : delta.getAffectedChildren()) {
				visit(c);
			}
		}
	};

	private final IPropertyChangeListener projectSelectedListener = new IPropertyChangeListener() {

		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			tableOfWarnings.setInput(lastSelectedProjectName);
		}

	};

	@Override
	public void createPartControl(final Composite parent) {
		tableOfWarnings = new TableViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableOfWarnings.setContentProvider(new WarningContentProvider());
		createColumns(parent, tableOfWarnings);
		lastSelectedProjectName = ProjectInfoProvider.getSelectedProject();
		tableOfWarnings.setInput(lastSelectedProjectName);

		// add all listeners
		getSite().getPage().addSelectionListener(projectSelectionListener);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(projectSelectedListener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectDeletedListener,
				IResourceChangeEvent.PRE_DELETE);
		JavaCore.addElementChangedListener(dependenciesChangedListener, ElementChangedEvent.POST_CHANGE);

		final Table table = tableOfWarnings.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	/*
	 * Create the columns
	 */
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

	/*
	 * Set the input of each column
	 */
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
		tableOfWarnings.getControl().setFocus();
	}

	@Override
	public void dispose() {
		super.dispose();

		// remove all listeners when view is closed
		getSite().getPage().removeSelectionListener(projectSelectionListener);
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(projectSelectedListener);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(projectDeletedListener);
		JavaCore.removeElementChangedListener(dependenciesChangedListener);
	}
}
