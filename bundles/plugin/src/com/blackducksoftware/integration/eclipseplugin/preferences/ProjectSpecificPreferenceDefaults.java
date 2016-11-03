package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.internal.Activator;

public class ProjectSpecificPreferenceDefaults extends PreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(final IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(final Composite parent) {
		// implement when project-specific preferences are fleshed out
		return null;
	}

	@Override
	public void performApply() {
		storeValues();
	}

	@Override
	public boolean performOk() {
		if (super.performOk()) {
			storeValues();
			return true;
		} else {
			return false;
		}
	}

	private void storeValues() {
		// implement when project-specific preferences are fleshed out
	}

	@Override
	protected void performDefaults() {
		// implement when project-specific preferences are fleshed out
	}

}
