package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.dialogs.AuthorizationDialog;
import com.blackducksoftware.integration.eclipseplugin.dialogs.AuthorizationValidator;

public class OpenBlackDuckAuthorizationDialog extends AbstractHandler {

	private final String TITLE = "Hub Authorization";
	private final String MESSAGE = "Enter your Hub credentials below:";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		String hubUrl;
		String username;
		String password;
		try {
			hubUrl = SecurePreferencesFactory.getDefault().node("Black Duck").get("activeHubUrl", null);
		} catch (final StorageException e) {
			hubUrl = null;
		}
		try {
			username = SecurePreferencesFactory.getDefault().node("Black Duck").get("activeUsername", null);
		} catch (final StorageException e) {
			username = null;
		}
		try {
			password = SecurePreferencesFactory.getDefault().node("Black Duck").get("activePassword", null);
		} catch (final StorageException e) {
			password = null;
		}
		final AuthorizationDialog authDialog = new AuthorizationDialog(activeShell, TITLE, MESSAGE, hubUrl, username,
				password, new AuthorizationValidator());
		authDialog.create();
		authDialog.open();
		return null;
	}

}
