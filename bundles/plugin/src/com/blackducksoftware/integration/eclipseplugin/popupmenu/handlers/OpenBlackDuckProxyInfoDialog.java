package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.common.constants.DialogTitles;
import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNodes;
import com.blackducksoftware.integration.eclipseplugin.common.services.SecurePreferencesService;
import com.blackducksoftware.integration.eclipseplugin.dialogs.ProxyServerInfoDialog;

public class OpenBlackDuckProxyInfoDialog extends AbstractHandler {

	private final String MESSAGE = "Enter your proxy server information below if you use one:";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final SecurePreferencesService prefService = new SecurePreferencesService(SecurePreferenceNodes.BLACK_DUCK,
				SecurePreferencesFactory.getDefault());
		final ProxyServerInfoDialog dialog = new ProxyServerInfoDialog(activeShell, prefService,
				DialogTitles.PROXY_INFO, MESSAGE);
		dialog.create();
		dialog.open();
		return null;
	}

}
