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
import com.blackducksoftware.integration.eclipseplugin.dialogs.ProxyServerInfoDialog;

public class OpenBlackDuckProxyInfoDialog extends AbstractHandler {

	private final String MESSAGE = "Enter your proxy server information below if you use one:";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final ISecurePreferences blackDuckNode = SecurePreferencesFactory.getDefault().node("Black Duck");
		String proxyUsername;
		String proxyPassword;
		String proxyPort;
		String proxyHost;
		String ignoredProxyHosts;
		try {
			proxyUsername = blackDuckNode.get("activeProxyUsername", "");
		} catch (final StorageException e) {
			proxyUsername = "";
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
			proxyHost = blackDuckNode.get("activeProxyHost", "");
		} catch (final StorageException e) {
			proxyHost = "";
		}
		try {
			ignoredProxyHosts = blackDuckNode.get("activeIgnoredProxyHosts", "");
		} catch (final StorageException e) {
			ignoredProxyHosts = "";
		}
		final ProxyServerInfoDialog dialog = new ProxyServerInfoDialog(activeShell, DialogTitles.PROXY_INFO, MESSAGE, proxyPassword,
				proxyPort, proxyUsername, proxyHost, ignoredProxyHosts);
		dialog.create();
		dialog.open();
		return null;
	}

}
