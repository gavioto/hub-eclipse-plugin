package com.blackducksoftware.integration.eclipseplugin.dialogs;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.builder.ValidationResult;
import com.blackducksoftware.integration.builder.ValidationResultEnum;
import com.blackducksoftware.integration.builder.ValidationResults;
import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;

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
		/*
		 * if (hubUrl == null || hubUrl.equals("") || username == null ||
		 * username.equals("") || password == null || password.equals("")) {
		 * return CREDENTIAL_MISSING_MESSAGE; } if (timeout == null ||
		 * timeout.equals("")) { return TIMEOUT_MISSING_MESSAGE; }
		 */
		final HubServerConfigBuilder builder = new HubServerConfigBuilder();
		setHubServerConfigBuilderFields(builder, username, password, hubUrl, proxyUsername, proxyPassword, proxyPort,
				proxyHost, ignoredProxyHosts, timeout);
		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		if (results.isSuccess()) {
			final RestConnection connection = connectionService.getRestConnection(hubUrl);
			try {
				connectionService.setCookies(connection, username, password);
				return LOGIN_SUCCESS_MESSAGE;
			} catch (final URISyntaxException e) {
				return e.getMessage();
			} catch (final BDRestException e) {
				return e.getMessage();
			}
		}

		final Map<GlobalFieldKey, List<ValidationResult>> resultMap = results.getResultMap();
		final Set<GlobalFieldKey> keySet = resultMap.keySet();
		final Iterator<GlobalFieldKey> keyIt = keySet.iterator();
		String errorMessage = null;
		while (keyIt.hasNext()) {
			final GlobalFieldKey k = keyIt.next();
			System.out.println("Key: " + k);
			final List<ValidationResult> resultList = resultMap.get(k);
			final Iterator<ValidationResult> resultIt = resultList.iterator();
			while (resultIt.hasNext()) {
				final ValidationResult result = resultIt.next();
				System.out.println("type: " + result.getResultType());
				System.out.println("message: " + result.getMessage());
				if (result.getResultType() == ValidationResultEnum.ERROR) {
					if (errorMessage == null && result.getMessage() != null) {
						errorMessage = result.getMessage();
					} else {
						errorMessage = StringUtils.join(new String[] { errorMessage, result.getMessage() }, '\n');
					}
				}
			}
			System.out.println();
		}
		System.out.println("---------------------------------");
		return errorMessage;
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
