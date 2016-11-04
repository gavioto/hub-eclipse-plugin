package com.blackducksoftware.integration.eclipseplugin.common.services;

import java.net.URISyntaxException;

import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.CredentialsRestConnection;

public class HubRestConnectionService {
	public CredentialsRestConnection getCredentialsRestConnection(final HubServerConfig config)
			throws IllegalArgumentException, URISyntaxException, BDRestException, EncryptionException {
		return new CredentialsRestConnection(config);
	}
}
