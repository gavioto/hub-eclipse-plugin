package com.blackducksoftware.integration.eclipseplugin.dialogs;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AuthorizationValidatorTest {

	@Test
	public void fieldsShouldNotBeNull() {
		final AuthorizationValidator validator = new AuthorizationValidator(null, null, null, null, null, null, null,
				null, null, false, false);
		assertNotNull(validator.getHubUrl());
		assertNotNull(validator.getUsername());
		assertNotNull(validator.getPassword());
		assertNotNull(validator.getProxyUsername());
		assertNotNull(validator.getProxyPassword());
		assertNotNull(validator.getProxyPort());
		assertNotNull(validator.getProxyHost());
		assertNotNull(validator.getIgnoredProxyHosts());
		assertNotNull(validator.getTimeout());
	}

	/*
	 * @Test public void testLoginAttemptResponses() { AuthorizationValidator
	 * validator = new AuthorizationValidator("", "", "", "", "", "", "", "",
	 * "", false, false); HubServerConfigBuilder builder =
	 * Mockito.mock(HubServerConfigBuilder.class);
	 * Mockito.when(builder.buildResults()).thenReturn(new
	 * ValidationResults<GlobalFieldKey, HubServerConfig>()); }
	 */
}
