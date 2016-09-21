package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectInfoProvider;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

public class ActiveJavaProjects extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		final String[] names = ProjectInfoProvider.getJavaProjectNames();
		for (final String name : names) {
			final BooleanFieldEditor activation = new BooleanFieldEditor(name, name, getFieldEditorParent());
			addField(activation);
		}
	}

	@Override
	public void init(final IWorkbench workbench) {
		// TODO Auto-generated method stub
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

}
