package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

/*
 * Class that implements the general Black Duck preferences page
 */
public class BlackDuckPreferences extends PreferencePage implements IWorkbenchPreferencePage {

	public BlackDuckPreferences() {
	}

	public BlackDuckPreferences(final String title) {
		super(title);
	}

	public BlackDuckPreferences(final String title, final ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(final IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(final Composite parent) {
		return null;
	}

}
