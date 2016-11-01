package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

public class PreferenceDefaults extends PreferencePage implements IWorkbenchPreferencePage {

	private final String ACTIVATE_BY_DEFAULT_LABEL = "Default Scan Activation Settings";
	private final String[][] DEFAULT_ACTIVATION_LABELS_AND_VALUES = new String[][] {
			new String[] { "Activate Scan by Default", "true" },
			new String[] { "Do Not Activate Scan by Default", "false" } };
	private RadioGroupFieldEditor activateByDefault;

	@Override
	public void init(final IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());

	}

	@Override
	protected Control createContents(final Composite parent) {
		final Composite defaultsComposite = new Composite(parent, SWT.LEFT);
		defaultsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		defaultsComposite.setLayout(new GridLayout());
		activateByDefault = new RadioGroupFieldEditor(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT,
				ACTIVATE_BY_DEFAULT_LABEL, 1, DEFAULT_ACTIVATION_LABELS_AND_VALUES, defaultsComposite);
		activateByDefault.setPreferenceStore(getPreferenceStore());
		activateByDefault.load();
		return defaultsComposite;
	}

	@Override
	public void performApply() {
		storeValues();
	}

	private void storeValues() {
		activateByDefault.store();
		System.out.println(getPreferenceStore().getString(PreferenceNames.ACTIVATE_SCAN_BY_DEFAULT));
	}

	@Override
	public boolean performOk() {
		storeValues();
		if (super.performOk()) {
			return true;
		}
		return false;
	}

	@Override
	protected void performDefaults() {
		activateByDefault.loadDefault();
		super.performDefaults();
	}

}
