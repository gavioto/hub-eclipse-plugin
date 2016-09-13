package com.blackducksoftware.integration.eclipseplugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningLabelProvider;
import org.eclipse.jface.viewers.TableViewer;

public class WarningView extends ViewPart {
	
	private TableViewer tableOfWarnings;
	private final Warning warning1 = new Warning("copmonent 1", 1, "match type 1", "usage 1", "license 1",
			"security risk 1", "operational risk 1");
	private final Warning warning2 = new Warning("copmonent 2", 1, "match type 2", "usage 2", "license 2",
			"security risk 2", "operational risk 2");
	private final Warning warning3 = new Warning("copmonent 3", 1, "match type 3", "usage 3", "license 3",
			"security risk 3", "operational risk 3");
	private final Warning warning4 = new Warning("copmonent 4", 1, "match type 4", "usage 4", "license 4",
			"security risk 4", "operational risk 4");
	private final Warning[] warnings = {warning1, warning2, warning3, warning4};

	@Override
	public void createPartControl(Composite parent) {
		Table table = new Table(parent, SWT.MULTI | SWT.BORDER);
		
		initializeTable(parent, table);
		
		tableOfWarnings = new TableViewer(table);
		// tableOfWarnings.setLabelProvider(new WarningLabelProvider());
		// tableOfWarnings.setContentProvider(new WarningContentProvider());
		
		populateTable(warnings, table);
		
		// eventually set input to local store
		// tableOfWarnings.setInput(warnings);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		tableOfWarnings.getControl().setFocus();
	}
	
	/*
	 * initialize the structure of the table
	 */
	private void initializeTable(Composite parent, Table table) {
		
		table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    
		TableColumn components = new TableColumn(table, SWT.RIGHT, 0);
		TableColumn matchCounts = new TableColumn(table, SWT.RIGHT, 1);
		TableColumn matchTypes = new TableColumn(table, SWT.RIGHT, 2);
		TableColumn usages = new TableColumn(table, SWT.RIGHT, 3);
		TableColumn licenses = new TableColumn(table, SWT.RIGHT, 4);
		TableColumn securityRisks = new TableColumn(table, SWT.RIGHT, 5);
		TableColumn operationalRisks = new TableColumn(table, SWT.RIGHT, 6);
		
		components.setText("Component");
		matchCounts.setText("Match Count");
		matchTypes.setText("Match Type");
		usages.setText("Usage");
		licenses.setText("License");
		securityRisks.setText("Security Risk");
		operationalRisks.setText("Operational Risk");
		
	}
	
	/*
	 * Extract content from array of warnings and populate table with it
	 */
	private void populateTable(Warning[] warnings, Table table) {
		
		for (int i = 0; i < warnings.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, warnings[i].getComponent());
			item.setText(1, warnings[i].getMatchCount());
			item.setText(2, warnings[i].getMatchType());
			item.setText(3, warnings[i].getUsage());
			item.setText(4, warnings[i].getLicense());
			item.setText(5, warnings[i].getSecurityRisk());
			item.setText(6, warnings[i].getOperationalRisk());
		}
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
	}

}
