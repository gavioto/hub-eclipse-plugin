package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectInfoProvider;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

/*
 * Class that implements preference page where user chooses which Java
 * projects to activate the Black Duck plugin for
 */
public class ActiveJavaProjects extends PreferencePage implements IWorkbenchPreferencePage {

	private BooleanFieldEditor[] activeProjectPreferences;

	@Override
	public void init(final IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(final Composite parent) {
		final Composite activeProjectsComposite = new Composite(parent, SWT.LEFT);
		activeProjectsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		activeProjectsComposite.setLayout(new GridLayout());

		final Label activeProjectsLabel = new Label(activeProjectsComposite, SWT.HORIZONTAL);
		activeProjectsLabel.setText("Active Java Projects");

		try {
			final String[] names = ProjectInfoProvider.getJavaProjectNames();
			activeProjectPreferences = new BooleanFieldEditor[names.length];
			for (int i = 0; i < names.length; i++) {
				final BooleanFieldEditor isActive = new BooleanFieldEditor(names[i], names[i], activeProjectsComposite);
				isActive.setPage(this);
				isActive.setPreferenceStore(getPreferenceStore());
				// default setting is to have scan enabled for every Java
				// project
				getPreferenceStore().setDefault(names[i], true);
				isActive.load();
				activeProjectPreferences[i] = isActive;
			}
			return activeProjectsComposite;
		} catch (final CoreException e) {
			e.printStackTrace();
			return activeProjectsComposite;
		}
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
		final IPreferenceStore prefStore = getPreferenceStore();
		for (final BooleanFieldEditor isActive : activeProjectPreferences) {
			prefStore.setValue(isActive.getPreferenceName(), isActive.getBooleanValue());
		}
	}

	@Override
	protected void performDefaults() {
		for (final BooleanFieldEditor isActive : activeProjectPreferences) {
			isActive.loadDefault();
		}
		super.performDefaults();
	}

}
