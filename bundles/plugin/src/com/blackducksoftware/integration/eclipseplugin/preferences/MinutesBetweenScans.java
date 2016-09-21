package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

public class MinutesBetweenScans extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(final IWorkbench workbench) {
		// obtain preferences
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		final IntegerFieldEditor minutesBetweenChecks = new IntegerFieldEditor("minutesBetweenChecks",
				"Minutes between checks", getFieldEditorParent());
		addField(minutesBetweenChecks);
	}

}
