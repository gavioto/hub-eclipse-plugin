package com.blackducksoftware.integration.eclipseplugin.internal;

import java.net.URISyntaxException;

import com.blackducksoftware.integration.builder.ValidationResultEnum;
import com.blackducksoftware.integration.builder.ValidationResults;
import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;

public class AuthorizationValidator {
	private final HubRestConnectionService connectionService;

	public static final String LOGIN_SUCCESS_MESSAGE = "Successful login!";
	public static final String INCORRECT_CREDENTIALS_MESSAGE = "Incorrect username or password. Please try again";
	public static final String LOGIN_ERROR_MESSAGE = "An error occurred while logging in";
	public static final String CREDENTIAL_MISSING_MESSAGE = "Please enter a hub URL instance, a username, and a password";
	public static final String TIMEOUT_MISSING_MESSAGE = "Please specify a timeout value";

	public AuthorizationValidator(final HubRestConnectionService connectionService) {
		this.connectionService = connectionService;
	}

	public String validateCredentials(final String username, final String password, final String hubUrl,
			final String proxyUsername, final String proxyPassword, final String proxyPort, final String proxyHost,
			final String ignoredProxyHosts, final String timeout) {
		final HubServerConfigBuilder builder = new HubServerConfigBuilder();
		setHubServerConfigBuilderFields(builder, username, password, hubUrl, proxyUsername, proxyPassword, proxyPort,
				proxyHost, ignoredProxyHosts, timeout);
		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		if (results.isSuccess()) {
			try {
				connectionService.getCredentialsRestConnection(results.getConstructedObject());
				return LOGIN_SUCCESS_MESSAGE;
			} catch (final IllegalArgumentException e) {
				return e.getMessage();
			} catch (final URISyntaxException e) {
				return e.getMessage();
			} catch (final BDRestException e) {
				return e.getMessage();
			} catch (final EncryptionException e) {
				return e.getMessage();
			}

		}
		return results.getAllResultString(ValidationResultEnum.ERROR);
	}

	private void setHubServerConfigBuilderFields(final HubServerConfigBuilder builder, final String username,
			final String password, final String hubUrl, final String proxyUsername, final String proxyPassword,
			final String proxyPort, final String proxyHost, final String ignoredProxyHosts, final String timeout) {
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
