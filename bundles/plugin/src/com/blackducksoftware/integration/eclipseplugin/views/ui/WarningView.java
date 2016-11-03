package com.blackducksoftware.integration.eclipseplugin.views.ui;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.Activator;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.PreferenceChangeDisplayUpdateListener;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.ProjectDeletedListener;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.ProjectDependenciesChangeListener;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.ProjectSelectionListener;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

/*
 * Class that implements the warning view UI
 */
public class WarningView extends ViewPart {

	private final int MAX_COLUMN_SIZE = 100;
	private TableViewer tableOfWarnings;
	private String lastSelectedProjectName = "";
	private PreferenceChangeDisplayUpdateListener preferenceChangeDisplayUpdateListener;
	private ProjectDeletedListener projectDeletedListener;
	private ProjectDependenciesChangeListener projectDependenciesChangeListener;
	private ProjectSelectionListener projectSelectionListener;
	private Display display;
	private IPreferenceStore prefs;
	private WorkspaceInformationService workspaceInformationService;

	@Override
	public void createPartControl(final Composite parent) {

		display = PlatformUI.getWorkbench().getDisplay();
		preferenceChangeDisplayUpdateListener = new PreferenceChangeDisplayUpdateListener(this, display);
		projectDeletedListener = new ProjectDeletedListener(this, display);
		projectDependenciesChangeListener = new ProjectDependenciesChangeListener(this, display);
		projectSelectionListener = new ProjectSelectionListener(this);
		prefs = Activator.getDefault().getPreferenceStore();
		workspaceInformationService = new WorkspaceInformationService(
				new ProjectInformationService(new DependencyInformationService(), new FilePathGavExtractor()));

		// add all listeners
		getSite().getPage().addSelectionListener(projectSelectionListener);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(preferenceChangeDisplayUpdateListener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectDeletedListener,
				IResourceChangeEvent.PRE_DELETE);
		JavaCore.addElementChangedListener(projectDependenciesChangeListener, ElementChangedEvent.POST_CHANGE);

		tableOfWarnings = new TableViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableOfWarnings.setContentProvider(new WarningContentProvider(prefs));
		createColumns(parent, tableOfWarnings);
		lastSelectedProjectName = workspaceInformationService.getSelectedProject();
		tableOfWarnings.setInput(lastSelectedProjectName);

		final Table table = tableOfWarnings.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	public void setLastSelectedProjectName(final String name) {
		this.lastSelectedProjectName = name;
	}

	public void setTableInput(final String input) {
		tableOfWarnings.setInput(input);
	}

	public String getLastSelectedProjectName() {
		return this.lastSelectedProjectName;
	}

	public TableViewer getTable() {
		return tableOfWarnings;
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
			viewCol.setLabelProvider(new WarningColumnLabelProvider(i));
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
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(preferenceChangeDisplayUpdateListener);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(projectDeletedListener);
		JavaCore.removeElementChangedListener(projectDependenciesChangeListener);
	}
}
