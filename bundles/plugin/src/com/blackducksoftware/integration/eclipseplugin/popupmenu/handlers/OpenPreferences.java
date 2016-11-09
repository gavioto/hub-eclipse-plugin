package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferencePageIds;

/*
 * Class that opens the general Black Duck preferences menu in a window
 */
public class OpenPreferences extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final String[] pageIds = new String[] { PreferencePageIds.BLACK_DUCK, PreferencePageIds.ACTIVE_JAVA_PROJECTS,
				PreferencePageIds.DEFAULT_CONFIG, PreferencePageIds.HUB_AUTHORIZATION };
		final PreferenceDialog prefPage = PreferencesUtil.createPreferenceDialogOn(activeShell,
				PreferencePageIds.BLACK_DUCK, pageIds, null);
		prefPage.open();
		return null;
	}

}
