package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

public class BlackDuckPreferences extends PreferencePage implements IWorkbenchPreferencePage {

	public BlackDuckPreferences() {
		// TODO Auto-generated constructor stub
	}

	public BlackDuckPreferences(final String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public BlackDuckPreferences(final String title, final ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(final IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());

	}

	@Override
	protected Control createContents(final Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
