package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

public class Scanning extends PreferencePage implements IWorkbenchPreferencePage {

	public Scanning() {
		// TODO Auto-generated constructor stub
	}

	public Scanning(final String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public Scanning(final String title, final ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(final IWorkbench workbench) {
		// obtain preferences
		setPreferenceStore(Activator.getDefault().getPreferenceStore());

	}

	@Override
	protected Control createContents(final Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
