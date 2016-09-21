package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenPreferences extends AbstractHandler {

	private final String BLACK_DUCK_PAGE_ID = "com.blackducksoftware.integration.eclipseplugin.preferences";
	private final String SCANNING_PAGE_ID = "com.blackducksoftware.integration.eclipseplugin.preferences.Scanning";
	private final String MINUTES_BETWEEN_SCANS_PAGE_ID = "com.blackducksoftware.integration.eclipseplugin.preferences.MinutesBetweenScans";
	private final String ACTIVE_JAVA_PROJECTS_PAGE_ID = "com.blackducksoftware.integration.eclipseplugin.preferences.ActiveJavaProjects";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final PreferenceDialog prefPage = PreferencesUtil.createPreferenceDialogOn(activeShell, BLACK_DUCK_PAGE_ID,
				new String[] { BLACK_DUCK_PAGE_ID, SCANNING_PAGE_ID, MINUTES_BETWEEN_SCANS_PAGE_ID,
						ACTIVE_JAVA_PROJECTS_PAGE_ID },
				null);
		prefPage.open();
		return null;

	}

}
