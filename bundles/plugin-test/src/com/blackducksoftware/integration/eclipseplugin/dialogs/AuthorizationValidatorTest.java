package com.blackducksoftware.integration.eclipseplugin.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.builder.ValidationResults;
import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubCredentials;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.rest.RestConnection;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationValidatorTest {

	@Mock
	ValidationResults<GlobalFieldKey, HubServerConfig> validationResults;
	@Mock
	HubServerConfigBuilder builder;
	@Mock
	HubRestConnectionService connectionService;
	@Mock
	HubServerConfig config;
	@Mock
	RestConnection connection;
	@Mock
	HubCredentials hubCredentials;

	private final String fakeUrl = "http://localhost:8080";
	private final String fakeUsername = "fakeUsername";
	private final String fakePassword = "fakePassword";

	@Test
	public void validatorSetsHubFieldsCorrectly() {
		assertTrue(true);
	}

	@Test
	public void testFailedValidationResults() {
		Mockito.when(validationResults.isSuccess()).thenReturn(false);
		Mockito.when(builder.buildResults()).thenReturn(validationResults);
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String response = validator.validateCredentials("username", "password", "url", null, null, null, null,
				null, "timeout");
		assertEquals("failed validation results does not return login error message", response,
				AuthorizationValidator.LOGIN_ERROR_MESSAGE);

	}

	@Test
	public void testNoUsername() {
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String nullResponse = validator.validateCredentials(null, "password", "hubUrl", null, null, null, null,
				null, "timeout");
		final String emptyStringResponse = validator.validateCredentials("", "password", "hubUrl", null, null, null,
				null, null, "timeout");
		assertEquals("null username does not return credential missing message", nullResponse,
				AuthorizationValidator.CREDENTIAL_MISSING_MESSAGE);
		assertEquals("empty username does not return credential missing message", emptyStringResponse,
				AuthorizationValidator.CREDENTIAL_MISSING_MESSAGE);
	}

	@Test
	public void testNoPassword() {
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String nullResponse = validator.validateCredentials("username", null, "hubUrl", null, null, null, null,
				null, "timeout");
		final String emptyStringResponse = validator.validateCredentials("username", "", "hubUrl", null, null, null,
				null, null, "timeout");
		assertEquals("null password does not return credential missing message", nullResponse,
				AuthorizationValidator.CREDENTIAL_MISSING_MESSAGE);
		assertEquals("empty password does not return credential missing message", emptyStringResponse,
				AuthorizationValidator.CREDENTIAL_MISSING_MESSAGE);
	}

	@Test
	public void testNoHubUrl() {
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String nullResponse = validator.validateCredentials("username", "password", null, null, null, null, null,
				null, "timeout");
		final String emptyStringResponse = validator.validateCredentials("username", "password", "", null, null, null,
				null, null, "timeout");
		assertEquals("null Hub URL does not return credential missing message", nullResponse,
				AuthorizationValidator.CREDENTIAL_MISSING_MESSAGE);
		assertEquals("empty Hub URL does not return credential missing message", emptyStringResponse,
				AuthorizationValidator.CREDENTIAL_MISSING_MESSAGE);
	}

	@Test
	public void testNoTimeout() {
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String nullResponse = validator.validateCredentials("username", "password", "hubUrl", null, null, null,
				null, null, null);
		final String emptyStringResponse = validator.validateCredentials("username", "password", "hubUrl", null, null,
				null, null, null, "");
		assertEquals("null timeout does not return credential missing message",
				AuthorizationValidator.TIMEOUT_MISSING_MESSAGE, nullResponse);
		assertEquals("empty timeout does not return credential missing message",
				AuthorizationValidator.TIMEOUT_MISSING_MESSAGE, emptyStringResponse);
	}

	@Test
	public void testReturnsLoginSuccessMessage() {
		setUpMocksForMessageTests();
		try {
			Mockito.doNothing().when(connectionService).setCookies(connection, fakeUsername, fakePassword);
		} catch (final URISyntaxException e) {
		} catch (final BDRestException e) {
		}
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String success = validator.validateCredentials(fakeUsername, fakePassword, fakeUrl, null, null, null,
				null, null, "timeout");
		assertEquals("success message not returned", AuthorizationValidator.LOGIN_SUCCESS_MESSAGE, success);
	}

	@Test
	public void testWhenSetCookiesThrowsIllegalArgumentException() {
		setUpMocksForMessageTests();
		try {
			Mockito.doThrow(IllegalArgumentException.class).when(connectionService).setCookies(connection, fakeUsername,
					fakePassword);
		} catch (final URISyntaxException e) {
		} catch (final BDRestException e) {
		}
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String error = validator.validateCredentials(fakeUsername, fakePassword, fakeUrl, null, null, null, null,
				null, "timeout");
		assertEquals("incorrect error/message returned", AuthorizationValidator.LOGIN_ERROR_MESSAGE, error);
	}

	@Test
	public void testWhenSetCookiesThrowsBDRestException() {
		setUpMocksForMessageTests();
		try {
			Mockito.doThrow(BDRestException.class).when(connectionService).setCookies(connection, fakeUsername,
					fakePassword);
		} catch (final URISyntaxException e) {
		} catch (final BDRestException e) {
		}
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String error = validator.validateCredentials(fakeUsername, fakePassword, fakeUrl, null, null, null, null,
				null, "timeout");
		assertEquals("incorrect error/message returned", AuthorizationValidator.INCORRECT_CREDENTIALS_MESSAGE, error);
	}

	@Test
	public void testWhenSetCookiesThrowsURISyntaxException() {
		setUpMocksForMessageTests();
		try {
			Mockito.doThrow(URISyntaxException.class).when(connectionService).setCookies(connection, fakeUsername,
					fakePassword);
		} catch (final URISyntaxException e) {
		} catch (final BDRestException e) {
		}
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String error = validator.validateCredentials(fakeUsername, fakePassword, fakeUrl, null, null, null, null,
				null, "timeout");
		assertEquals("incorrect error/message returned", AuthorizationValidator.LOGIN_ERROR_MESSAGE, error);
	}

	@Test
	public void testWhenSetCookiesThrowsEncryptionException() {
		setUpMocksForMessageTests();
		try {
			Mockito.doThrow(EncryptionException.class).when(connectionService).setCookies(connection, fakeUsername,
					fakePassword);
		} catch (final URISyntaxException e) {
		} catch (final BDRestException e) {
		}
		final AuthorizationValidator validator = new AuthorizationValidator(builder, connectionService);
		final String error = validator.validateCredentials(fakeUsername, fakePassword, fakeUrl, null, null, null, null,
				null, "timeout");
		assertEquals("incorrect error/message returned", AuthorizationValidator.LOGIN_ERROR_MESSAGE, error);
	}

	private void setUpMocksForMessageTests() {
		Mockito.when(validationResults.isSuccess()).thenReturn(true);
		Mockito.when(validationResults.getConstructedObject()).thenReturn(config);
		try {
			Mockito.when(config.getHubUrl()).thenReturn(new URL(fakeUrl));
		} catch (final MalformedURLException e1) {
		}
		Mockito.when(config.getGlobalCredentials()).thenReturn(hubCredentials);
		Mockito.when(builder.buildResults()).thenReturn(validationResults);
		Mockito.when(config.getGlobalCredentials().getUsername()).thenReturn(fakeUsername);
		try {
			Mockito.when(config.getGlobalCredentials().getDecryptedPassword()).thenReturn(fakePassword);
		} catch (final IllegalArgumentException e) {
		} catch (final EncryptionException e) {
		}
		Mockito.when(connectionService.getRestConnection(fakeUrl)).thenReturn(connection);
	}
}
