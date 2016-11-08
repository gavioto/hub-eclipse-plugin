package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class PreferenceChangeDisplayUpdateListener implements IPropertyChangeListener {

	private final WarningView warningView;

	public PreferenceChangeDisplayUpdateListener(final WarningView warningView) {
		this.warningView = warningView;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (warningView.getTable() != null) {
			warningView.resetInput();
		}
	}
}
