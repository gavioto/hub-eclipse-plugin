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
import com.blackducksoftware.integration.eclipseplugin.startup.Activator;

public class PreferenceDefaults extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String ACTIVATE_BY_DEFAULT_LABEL = "Default Scan Activation Settings";
	public static final String ACTIVATE_BY_DEFAULT = "Activate Scan by Default";
	public static final String DO_NOT_ACTIVATE_BY_DEFAULT = "Do Not Activate Scan by Default";
	private final String[][] DEFAULT_ACTIVATION_LABELS_AND_VALUES = new String[][] {
			new String[] { ACTIVATE_BY_DEFAULT, "true" }, new String[] { DO_NOT_ACTIVATE_BY_DEFAULT, "false" } };
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
