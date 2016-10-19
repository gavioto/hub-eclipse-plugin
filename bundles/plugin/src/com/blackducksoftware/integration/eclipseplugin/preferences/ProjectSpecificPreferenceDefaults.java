package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.common.constants.ProjectSpecificPreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

public class ProjectSpecificPreferenceDefaults extends PreferencePage implements IWorkbenchPreferencePage {

	private RadioGroupFieldEditor activateAutomatically;

	@Override
	public void init(final IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(final Composite parent) {
		final Composite automaticActivationComposite = new Composite(parent, SWT.LEFT);
		automaticActivationComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		automaticActivationComposite.setLayout(new GridLayout());

		final Label automaticActivationLabel = new Label(automaticActivationComposite, SWT.HORIZONTAL);
		automaticActivationLabel.setText("Activate Black Duck");
		final String[][] labelsAndValues = new String[][] { { "Activate Scan Automatically", "true" },
				{ "Do Not Activate Scan Automatically", "false" } };
		activateAutomatically = new RadioGroupFieldEditor(ProjectSpecificPreferenceNames.ACTIVATE_PROJECT,
				"Activate Scan Automatically for New Projects", 2, labelsAndValues, automaticActivationComposite);
		activateAutomatically.setPage(this);
		activateAutomatically.setPreferenceStore(Activator.getDefault().getPreferenceStore());
		activateAutomatically.load();
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
		activateAutomatically.store();
	}

	@Override
	protected void performDefaults() {
		activateAutomatically.loadDefault();
	}

}
