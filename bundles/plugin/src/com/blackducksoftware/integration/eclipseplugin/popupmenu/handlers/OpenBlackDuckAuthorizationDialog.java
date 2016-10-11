package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.constants.DialogTitles;
import com.blackducksoftware.integration.eclipseplugin.dialogs.AuthorizationDialog;

public class OpenBlackDuckAuthorizationDialog extends AbstractHandler {

	private final String MESSAGE = "Enter your Hub credentials below:";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		String hubUrl;
		String username;
		String password;
		String proxyPassword;
		String proxyPort;
		String proxyUsername;
		String proxyHost;
		String ignoredProxyHosts;
		String timeout;
		final ISecurePreferences blackDuckNode = SecurePreferencesFactory.getDefault().node("Black Duck");
		try {
			hubUrl = blackDuckNode.get("activeHubUrl", "");
		} catch (final StorageException e) {
			hubUrl = "";
		}
		try {
			username = blackDuckNode.get("activeUsername", "");
		} catch (final StorageException e) {
			username = "";
		}
		try {
			password = blackDuckNode.get("activePassword", "");
		} catch (final StorageException e) {
			password = "";
		}
		try {
			proxyPassword = blackDuckNode.get("activeProxyPassword", "");
		} catch (final StorageException e) {
			proxyPassword = "";
		}
		try {
			proxyPort = blackDuckNode.get("activeProxyPort", "");
		} catch (final StorageException e) {
			proxyPort = "";
		}
		try {
			proxyUsername = blackDuckNode.get("activeProxyUsername", "");
		} catch (final StorageException e) {
			proxyUsername = "";
		}
		try {
			proxyHost = blackDuckNode.get("activeProxyHost", "");
		} catch (final StorageException e) {
			proxyHost = "";
		}
		try {
			ignoredProxyHosts = blackDuckNode.get("activeIgnoredProxyHosts", "");
		} catch (final StorageException e) {
			ignoredProxyHosts = "";
		}
		try {
			timeout = blackDuckNode.get("activeTimeout", "");
		} catch (final StorageException e) {
			timeout = "";
		}
		final AuthorizationDialog authDialog = new AuthorizationDialog(activeShell, DialogTitles.HUB_AUTHORIZATION, MESSAGE, hubUrl, username,
				password, proxyPassword, proxyPort, proxyUsername, proxyHost, ignoredProxyHosts, timeout);
		authDialog.create();
		authDialog.open();
		return null;
	}

}
