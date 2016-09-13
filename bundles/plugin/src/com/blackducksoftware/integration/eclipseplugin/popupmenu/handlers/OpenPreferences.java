package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenPreferences extends AbstractHandler {

	private final String PREF_PAGE_ID = "com.blackducksoftware.integration.eclipseplugin.preferences.ScanningPreferences";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final PreferenceDialog prefPage = PreferencesUtil.createPreferenceDialogOn(activeShell, PREF_PAGE_ID,
				new String[] { PREF_PAGE_ID }, null);
		prefPage.open();
		return null;

	}

}
