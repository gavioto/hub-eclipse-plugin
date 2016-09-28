package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

/*
 * Class that opens the general Black Duck preferences menu in a window
 */
public class OpenPreferences extends AbstractHandler {

	private final String BLACK_DUCK_PAGE_ID = "BlackDuck";
	private final String ACTIVE_JAVA_PROJECTS_PAGE_ID = "ActiveJavaProjects";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final String[] pageIds = new String[] { BLACK_DUCK_PAGE_ID, ACTIVE_JAVA_PROJECTS_PAGE_ID };
		final PreferenceDialog prefPage = PreferencesUtil.createPreferenceDialogOn(activeShell, BLACK_DUCK_PAGE_ID,
				pageIds, null);
		prefPage.open();
		return null;
	}

}
