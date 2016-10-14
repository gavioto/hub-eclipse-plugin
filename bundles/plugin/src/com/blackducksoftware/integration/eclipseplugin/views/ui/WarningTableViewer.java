package com.blackducksoftware.integration.eclipseplugin.views.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class WarningTableViewer extends TableViewer {

	private String lastProjectSelectedName;

	public WarningTableViewer(final Composite parent, final int style) {
		super(parent, style);
		lastProjectSelectedName = "";
	}

	public WarningTableViewer(final Composite parent) {
		super(parent);
		lastProjectSelectedName = "";
	}

	public WarningTableViewer(final Table table) {
		super(table);
		lastProjectSelectedName = "";
	}

	public void setLastProjectSelectedName(final String name) {
		lastProjectSelectedName = name;
	}

	public String getLastProjectSelectedName() {
		return lastProjectSelectedName;
	}

}
