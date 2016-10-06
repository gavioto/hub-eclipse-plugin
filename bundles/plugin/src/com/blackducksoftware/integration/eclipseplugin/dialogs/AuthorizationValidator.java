package com.blackducksoftware.integration.eclipseplugin.dialogs;

import java.io.IOException;
import java.net.URISyntaxException;

import com.blackducksoftware.integration.builder.ValidationResults;
import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.HubIntRestService;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;

public class AuthorizationValidator {

	private String timeout;

	public String isValid(final String hubUrl, final String username, final String password, final String proxyPassword,
			final String proxyPort, final String proxyUsername, final String proxyHost, final String ignoredProxyHosts,
			final String timeout, final boolean useDefaultTimeout, final boolean useProxyValues) {
		if (hubUrl == null || hubUrl.equals("") || username == null || username.equals("") || password == null
				|| password.equals("")) {
			return AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE;
		}

		final HubServerConfigBuilder builder = new HubServerConfigBuilder(true);
		builder.setUsername(username);
		builder.setPassword(password);
		builder.setHubUrl(hubUrl);
		if (useDefaultTimeout) {
			builder.setTimeout("120");
		} else {
			builder.setTimeout(timeout);
		}
		if (!useProxyValues) {
			builder.setProxyPassword("");
			builder.setProxyPort("");
			builder.setProxyUsername("");
			builder.setProxyHost("");
			builder.setIgnoredProxyHosts("");
		} else {
			if (proxyPassword != null) {
				builder.setProxyPassword(proxyPassword);
			} else {
				builder.setProxyPassword("");
			}
			if (proxyPort != null) {
				builder.setProxyPort(proxyPort);
			} else {
				builder.setProxyPort("");
			}
			if (proxyUsername != null) {
				builder.setProxyUsername(proxyUsername);
			} else {
				builder.setProxyUsername("");
			}
			if (proxyHost != null) {
				builder.setProxyHost(proxyHost);
			} else {
				builder.setProxyHost("");
			}
			if (ignoredProxyHosts != null) {
				builder.setIgnoredProxyHosts(ignoredProxyHosts);
			} else {
				builder.setIgnoredProxyHosts("");
			}
		}

		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		this.timeout = builder.getTimeout();
		if (results.isSuccess()) {
			final HubServerConfig config = results.getConstructedObject();
			final RestConnection connection = new RestConnection(config.getHubUrl().toString());
			try {
				connection.setCookies(config.getGlobalCredentials().getUsername(),
						config.getGlobalCredentials().getDecryptedPassword());
				final HubIntRestService restService = new HubIntRestService(connection);
				try {
					final String hubVersion = restService.getHubVersion();
					System.out.println(hubVersion);
					return AuthorizationDialog.LOGIN_SUCCESS_MESSAGE;
				} catch (final IOException e) {
					return e.getMessage();
				}
			} catch (final IllegalArgumentException e) {
				return AuthorizationDialog.LOGIN_ERROR_MESSAGE;
			} catch (final URISyntaxException e) {
				return AuthorizationDialog.LOGIN_ERROR_MESSAGE;
			} catch (final BDRestException e) {
				return AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE;
			} catch (final EncryptionException e) {
				return AuthorizationDialog.LOGIN_ERROR_MESSAGE;
			}
		}
		return AuthorizationDialog.LOGIN_ERROR_MESSAGE;
	}

	public String getTimeout() {
		return this.timeout;
	}

}
