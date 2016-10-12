package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;
import com.blackducksoftware.integration.eclipseplugin.preferences.IndividualProjectPreferences;

/*
 * Class to create a window in which user can modify project-specific Black Duck preferences
 */
public class OpenProjectPreferences extends AbstractHandler {

	private final char preferencePathSeparatorCharacter = '.';

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final PreferenceManager mgr = new PreferenceManager(preferencePathSeparatorCharacter);
		final String projectPrefId = WorkspaceInformationService.getSelectedProject();
		final IndividualProjectPreferences prefPage = new IndividualProjectPreferences(projectPrefId, projectPrefId);
		final PreferenceNode prefNode = new PreferenceNode(projectPrefId, prefPage);
		mgr.addToRoot(prefNode);
		final PreferenceDialog prefDialog = new PreferenceDialog(activeShell, mgr);
		prefDialog.open();
		return null;
	}

}
