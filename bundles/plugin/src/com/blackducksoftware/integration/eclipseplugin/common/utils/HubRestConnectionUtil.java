package com.blackducksoftware.integration.eclipseplugin.common.utils;

import java.net.URISyntaxException;

import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.rest.RestConnection;

public class HubRestConnectionUtil {
	public RestConnection getRestConnection(final String url) {
		return new RestConnection(url);
	}

	public void setCookies(final RestConnection connection, final String username, final String password)
			throws URISyntaxException, BDRestException {
		connection.setCookies(username, password);
	}
}
