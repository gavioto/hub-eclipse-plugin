package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class PreferenceChangeDisplayUpdateListener implements IPropertyChangeListener {

	private final WarningView warningView;
	private final Display display;

	public PreferenceChangeDisplayUpdateListener(final WarningView warningView, final Display display) {
		this.warningView = warningView;
		this.display = display;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (warningView.getTable() != null) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					warningView.setTableInput(warningView.getLastSelectedProjectName());
				}
			});

		}
	}
}
