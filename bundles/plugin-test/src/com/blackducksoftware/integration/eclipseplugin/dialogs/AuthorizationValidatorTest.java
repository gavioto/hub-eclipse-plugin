package com.blackducksoftware.integration.eclipseplugin.dialogs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.rest.RestConnection;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationValidatorTest {

	@Mock
	HubRestConnectionService connectionService;
	@Mock
	RestConnection connection;

	private final String FAKE_URL = "http://www.google.com/";
	private final String FAKE_USERNAME = "fakeUsername";
	private final String FAKE_PASSWORD = "fakePassword";
	private final String FAKE_TIMEOUT = "120";
	private final String BD_REST_EXCEPTION_MESSAGE = "BDRestException message";
	private final String URI_SYNTAX_EXCEPTION_MESSAGE = "URISyntaxException message";
	private final String ERR_NO_USERNAME = "No Hub Username was found.";
	private final String ERR_NO_PASSWORD = "No Hub Password was found.";
	private final String ERR_NO_TIMEOUT = "No Hub Timeout was found.";
	private final String ERR_TIMEOUT_ZERO = "The Timeout must be greater than 0.";

	@Test
	public void testNoUsername() {
		final AuthorizationValidator validator = new AuthorizationValidator(connectionService);
		final String message = validator.validateCredentials(null, FAKE_PASSWORD, FAKE_URL, null, null, null, null,
				null, FAKE_TIMEOUT);
		assertEquals(ERR_NO_USERNAME, message);

	}

	@Test
	public void testNoPassword() {
		final AuthorizationValidator validator = new AuthorizationValidator(connectionService);
		final String message = validator.validateCredentials(FAKE_USERNAME, null, FAKE_URL, null, null, null, null,
				null, FAKE_TIMEOUT);
		assertEquals(ERR_NO_PASSWORD, message);
	}

	@Test
	public void testNoHubUrl() {
		final AuthorizationValidator validator = new AuthorizationValidator(connectionService);
		final String message = validator.validateCredentials(FAKE_USERNAME, FAKE_PASSWORD, null, null, null, null, null,
				null, FAKE_TIMEOUT);
		assertEquals(HubServerConfigBuilder.ERROR_MSG_URL_NOT_FOUND, message);
	}

	@Test
	public void testNoTimeout() {
		final AuthorizationValidator validator = new AuthorizationValidator(connectionService);
		final String message = validator.validateCredentials(FAKE_USERNAME, FAKE_PASSWORD, FAKE_URL, null, null, null,
				null, null, null);
		assertEquals(ERR_NO_TIMEOUT, message);
	}

	@Test
	public void testZeroTimeout() {
		final AuthorizationValidator validator = new AuthorizationValidator(connectionService);
		final String message = validator.validateCredentials(FAKE_USERNAME, FAKE_PASSWORD, FAKE_URL, null, null, null,
				null, null, "0");
		assertEquals(ERR_TIMEOUT_ZERO, message);
	}

	@Test
	public void testReturnsLoginSuccessMessage() {
		final AuthorizationValidator validator = new AuthorizationValidator(connectionService);
		final String message = validator.validateCredentials(FAKE_USERNAME, FAKE_PASSWORD, FAKE_URL, null, null, null,
				null, null, FAKE_TIMEOUT);
		assertEquals(AuthorizationValidator.LOGIN_SUCCESS_MESSAGE, message);

	}
}
