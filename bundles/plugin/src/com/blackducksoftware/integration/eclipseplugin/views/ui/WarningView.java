package com.blackducksoftware.integration.eclipseplugin.views.ui;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
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
import com.blackducksoftware.integration.eclipseplugin.startup.Activator;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.PreferenceChangeDisplayUpdateListener;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.ProjectDeletedListener;
import com.blackducksoftware.integration.eclipseplugin.views.listeners.ProjectSelectionListener;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningColumnLabelProvider;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

public class WarningView extends ViewPart {

    private final int MAX_COLUMN_SIZE = 100;

    private TableViewer tableOfWarnings;

    private String lastSelectedProjectName = "";

    private PreferenceChangeDisplayUpdateListener preferenceChangeDisplayUpdateListener;

    private ProjectDeletedListener projectDeletedListener;

    private ProjectSelectionListener projectSelectionListener;

    private Display display;

    private IPreferenceStore prefs;

    private WorkspaceInformationService workspaceInformationService;

    public static final String VULNERABILITY_NAME_LABEL = "Vulnerability Name";

    public static final String VULNERABILITY_DESCRIPTION_LABEL = "Vulnerability Description";

    public static final String VULNERABILITY_BASE_SCORE_LABEL = "Vulnerability Base Score";

    public static final String VULNERABILITY_SEVERITY_LABEL = "Vulnerability Severity";

    @Override
    public void createPartControl(final Composite parent) {

        display = PlatformUI.getWorkbench().getDisplay();
        // preferenceChangeDisplayUpdateListener = new PreferenceChangeDisplayUpdateListener(this);
        // projectDeletedListener = new ProjectDeletedListener(this);
        // projectSelectionListener = new ProjectSelectionListener(this);
        prefs = Activator.getDefault().getPreferenceStore();
        workspaceInformationService = new WorkspaceInformationService(
                new ProjectInformationService(new DependencyInformationService(), new FilePathGavExtractor()));

        getSite().getPage().addSelectionListener(projectSelectionListener);
        Activator.getDefault().getPreferenceStore().addPropertyChangeListener(preferenceChangeDisplayUpdateListener);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(projectDeletedListener,
                IResourceChangeEvent.PRE_DELETE);
        // Activator.getDefault().getProjectInformation().setComponentView(this);

        tableOfWarnings = new TableViewer(parent,
                SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        tableOfWarnings
                .setContentProvider(new WarningContentProvider(prefs, Activator.getDefault().getProjectInformation()));
        createColumns(parent, tableOfWarnings);
        lastSelectedProjectName = workspaceInformationService.getSelectedProject();
        tableOfWarnings.setInput(lastSelectedProjectName);

        final Table table = tableOfWarnings.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
    }

    public void resetInput() {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                tableOfWarnings.setInput(lastSelectedProjectName);
            }
        });
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

    private void createColumns(final Composite parent, final TableViewer warningTable) {
        final String[] labels = { VULNERABILITY_NAME_LABEL, VULNERABILITY_DESCRIPTION_LABEL, VULNERABILITY_BASE_SCORE_LABEL, VULNERABILITY_SEVERITY_LABEL };
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
        // getSite().getPage().removeSelectionListener(projectSelectionListener);
        // Activator.getDefault().getPreferenceStore().removePropertyChangeListener(preferenceChangeDisplayUpdateListener);
        // ResourcesPlugin.getWorkspace().removeResourceChangeListener(projectDeletedListener);
        // Activator.getDefault().getProjectInformation().removeComponentView();
    }
}
