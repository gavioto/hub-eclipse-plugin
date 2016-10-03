package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
		final AuthorizationDialog authDialog = new AuthorizationDialog(activeShell, TITLE, MESSAGE, "",
				new AuthorizationValidator());
		authDialog.create();
		authDialog.open();
		return null;
	}

}
