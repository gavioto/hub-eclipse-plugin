package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNodes;
import com.blackducksoftware.integration.eclipseplugin.common.utils.HubRestConnectionUtil;
import com.blackducksoftware.integration.eclipseplugin.common.utils.SecurePreferencesService;
import com.blackducksoftware.integration.eclipseplugin.dialogs.AuthorizationDialog;
import com.blackducksoftware.integration.eclipseplugin.dialogs.AuthorizationValidator;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;

public class OpenBlackDuckAuthorizationDialog extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final SecurePreferencesService prefService = new SecurePreferencesService(SecurePreferenceNodes.BLACK_DUCK,
				SecurePreferencesFactory.getDefault());
		final HubServerConfigBuilder builder = new HubServerConfigBuilder(false);
		final AuthorizationValidator validator = new AuthorizationValidator(builder, new HubRestConnectionUtil());
		final AuthorizationDialog authDialog = new AuthorizationDialog(activeShell, prefService, validator);
		authDialog.create();
		authDialog.open();
		return null;
	}

}
