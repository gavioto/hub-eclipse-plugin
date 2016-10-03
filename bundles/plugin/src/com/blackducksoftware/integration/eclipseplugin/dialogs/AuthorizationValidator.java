package com.blackducksoftware.integration.eclipseplugin.dialogs;

import java.net.URISyntaxException;

import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.builder.ValidationResults;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.exception.EncryptionException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;

public class AuthorizationValidator {

	public String isValid(final String hubUrl, final String username, final String password) {
		if (hubUrl == null || hubUrl.equals("") || username == null || username.equals("") || password == null
				|| password.equals("")) {
			return AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE;
		}
		final HubServerConfigBuilder builder = new HubServerConfigBuilder(true);
		builder.setUsername(username);
		builder.setPassword(password);
		builder.setHubUrl(hubUrl);
		builder.setTimeout(120);
		builder.setProxyPassword("");
		builder.setProxyPort("");
		builder.setProxyUsername("");
		builder.setProxyHost("");
		builder.setIgnoredProxyHosts("");
		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		if (results.isSuccess()) {
			final HubServerConfig config = results.getConstructedObject();
			final RestConnection connection = new RestConnection(config.getHubUrl().toString());
			try {
				connection.setCookies(config.getGlobalCredentials().getUsername(),
						config.getGlobalCredentials().getDecryptedPassword());
				return AuthorizationDialog.LOGIN_SUCCESS_MESSAGE;
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

}
