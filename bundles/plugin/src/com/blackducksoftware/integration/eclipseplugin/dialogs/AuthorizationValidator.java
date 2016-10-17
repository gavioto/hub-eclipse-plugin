package com.blackducksoftware.integration.eclipseplugin.dialogs;

import java.net.URISyntaxException;

import com.blackducksoftware.integration.builder.ValidationResults;
import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;

public class AuthorizationValidator {

	private final HubServerConfigBuilder builder;
	private final HubRestConnectionService connectionService;

	public static final String LOGIN_SUCCESS_MESSAGE = "Successful login!";
	public static final String INCORRECT_CREDENTIALS_MESSAGE = "Incorrect username or password. Please try again";
	public static final String LOGIN_ERROR_MESSAGE = "An error occurred while logging in";
	public static final String CREDENTIAL_MISSING_MESSAGE = "Please enter a hub URL instance, a username, and a password";
	public static final String TIMEOUT_MISSING_MESSAGE = "Please specify a timeout value";

	public AuthorizationValidator(final HubServerConfigBuilder builder,
			final HubRestConnectionService connectionService) {
		this.builder = builder;
		this.connectionService = connectionService;
	}

	public String validateCredentials(final String username, final String password, final String hubUrl,
			final String proxyUsername, final String proxyPassword, final String proxyPort, final String proxyHost,
			final String ignoredProxyHosts, final String timeout) {
		if (hubUrl == null || hubUrl.equals("") || username == null || username.equals("") || password == null
				|| password.equals("")) {
			return CREDENTIAL_MISSING_MESSAGE;
		}
		if (timeout == null || timeout.equals("")) {
			return TIMEOUT_MISSING_MESSAGE;
		}
		setHubServerConfigBuilderFields(username, password, hubUrl, proxyUsername, proxyPassword, proxyPort, proxyHost,
				ignoredProxyHosts, timeout);
		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		if (results.isSuccess()) {
			final HubServerConfig config = results.getConstructedObject();
			final RestConnection connection = connectionService.getRestConnection(config.getHubUrl().toString());
			try {
				connectionService.setCookies(connection, config.getGlobalCredentials().getUsername(),
						config.getGlobalCredentials().getDecryptedPassword());
				return LOGIN_SUCCESS_MESSAGE;
			} catch (final IllegalArgumentException e) {
				return LOGIN_ERROR_MESSAGE;
			} catch (final URISyntaxException e) {
				return LOGIN_ERROR_MESSAGE;
			} catch (final BDRestException e) {
				return INCORRECT_CREDENTIALS_MESSAGE;
			} catch (final EncryptionException e) {
				return LOGIN_ERROR_MESSAGE;
			}
		}
		return LOGIN_ERROR_MESSAGE;
	}

	private void setHubServerConfigBuilderFields(final String username, final String password, final String hubUrl,
			final String proxyUsername, final String proxyPassword, final String proxyPort, final String proxyHost,
			final String ignoredProxyHosts, final String timeout) {
		builder.setUsername(username);
		builder.setPassword(password);
		builder.setHubUrl(hubUrl);
		builder.setTimeout(timeout);
		builder.setProxyUsername(proxyUsername);
		builder.setProxyPassword(proxyPassword);
		builder.setProxyHost(proxyHost);
		builder.setProxyPort(proxyPort);
		builder.setIgnoredProxyHosts(ignoredProxyHosts);
	}
}
