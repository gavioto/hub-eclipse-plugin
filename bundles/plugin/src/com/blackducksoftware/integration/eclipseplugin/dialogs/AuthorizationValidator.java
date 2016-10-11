package com.blackducksoftware.integration.eclipseplugin.dialogs;

import java.net.URISyntaxException;

import com.blackducksoftware.integration.builder.ValidationResults;
import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;

public class AuthorizationValidator {

	private final String hubUrl;
	private final String username;
	private final String password;
	private String proxyPassword;
	private String proxyPort;
	private String proxyUsername;
	private String proxyHost;
	private String ignoredProxyHosts;
	private String timeout;

	private final String DEFAULT_TIMEOUT = "120";

	public AuthorizationValidator(final String hubUrl, final String username, final String password,
			final String proxyPassword, final String proxyPort, final String proxyUsername, final String proxyHost,
			final String ignoredProxyHosts, final String timeout, final boolean useDefaultTimeout,
			final boolean useProxyValues) {
		if (hubUrl == null) {
			this.hubUrl = "";
		} else {
			this.hubUrl = hubUrl;
		}
		if (username == null) {
			this.username = "";
		} else {
			this.username = username;
		}
		if (password == null) {
			this.password = "";
		} else {
			this.password = password;
		}
		if (useProxyValues) {
			if (proxyPassword == null) {
				this.proxyPassword = "";
			} else {
				this.proxyPassword = proxyPassword;
			}
			if (proxyPort == null) {
				this.proxyPort = "";
			} else {
				this.proxyPort = proxyPort;
			}
			if (proxyUsername == null) {
				this.proxyUsername = "";
			} else {
				this.proxyUsername = proxyUsername;
			}
			if (proxyHost == null) {
				this.proxyHost = "";
			} else {
				this.proxyHost = proxyHost;
			}
			if (ignoredProxyHosts == null) {
				this.ignoredProxyHosts = "";
			} else {
				this.ignoredProxyHosts = ignoredProxyHosts;
			}
		} else {
			this.proxyPassword = "";
			this.proxyPort = "";
			this.proxyUsername = "";
			this.proxyHost = "";
			this.ignoredProxyHosts = "";
		}
		if (useDefaultTimeout) {
			this.timeout = DEFAULT_TIMEOUT;
		} else {
			if (timeout == null) {
				this.timeout = DEFAULT_TIMEOUT;
			} else {
				this.timeout = timeout;
			}
		}
	}

	public String isValid() {
		if (hubUrl == null || hubUrl.equals("") || username == null || username.equals("") || password == null
				|| password.equals("")) {
			return AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE;
		}

		final HubServerConfigBuilder builder = new HubServerConfigBuilder(true);
		setFields(builder);
		try {
			final boolean loginSuccess = login(builder);
			if (loginSuccess) {
				return AuthorizationDialog.LOGIN_SUCCESS_MESSAGE;
			} else {
				return AuthorizationDialog.LOGIN_ERROR_MESSAGE;
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

	public boolean login(final HubServerConfigBuilder builder)
			throws IllegalArgumentException, URISyntaxException, BDRestException, EncryptionException {
		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		if (results.isSuccess()) {
			final HubServerConfig config = results.getConstructedObject();
			final RestConnection connection = new RestConnection(config.getHubUrl().toString());
			connection.setCookies(config.getGlobalCredentials().getUsername(),
					config.getGlobalCredentials().getDecryptedPassword());
		}
		return results.isSuccess();
	}

	private void setFields(final HubServerConfigBuilder builder) {
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

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getHubUrl() {
		return hubUrl;
	}

	public String getProxyUsername() {
		return this.proxyUsername;
	}

	public String getProxyPassword() {
		return this.proxyPassword;
	}

	public String getProxyPort() {
		return this.proxyPort;
	}

	public String getProxyHost() {
		return this.proxyHost;
	}

	public String getIgnoredProxyHosts() {
		return this.ignoredProxyHosts;
	}

	public String getTimeout() {
		return this.timeout;
	}

}
