package com.blackducksoftware.integration.eclipseplugin.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

public class AuthorizationValidatorTest {

	private final AuthorizationValidator validator = new AuthorizationValidator();

	@Test
	public void testHubAuthorizationFailure() {
		final String badUsername = validator.isValid("http://eng-hub-valid01.dc1.lan/", "fake", "blackduck", null, null,
				null, null, null, null, false, false);
		final String badPassword = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "fake", null, null,
				null, null, null, null, false, false);
		final String badUsernameAndPassword = validator.isValid("http://eng-hub-valid01.dc1.lan/", "f", "f", null, null,
				null, null, null, null, false, false);
		assertEquals(badUsername, AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE);
		assertEquals(badPassword, AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE);
		assertEquals(badUsernameAndPassword, AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE);
	}

	// not sure whether this is testable without actual proxy
	@Ignore
	public void testProxyCredentialValidation() {
	}

	@Test
	public void testHubAuthorizationSuccess() {
		final String success1 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null,
				null, null, null, null, null, true, false);
		final String success2 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null,
				null, null, null, null, null, false, false);
		final String success3 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null,
				null, null, null, null, null, true, true);
		final String success4 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null,
				null, null, null, null, null, false, true);
		final String success5 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null,
				null, null, null, null, "200", true, false);
		final String success6 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null,
				null, null, null, null, "200", false, false);
		final String success7 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", "fake",
				"fake", "fake", "fake", "fake", "300", true, false);
		final String success8 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", "fake",
				"fake", "fake", "fake", "fake", "300", false, false);
		assertEquals(success1, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success2, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success3, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success4, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success5, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success6, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success7, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(success8, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
	}

	@Test
	public void testRequiredParamMissingOutput() {
		final String nullTestString = validator.isValid(null, null, null, null, null, null, null, null, null, false,
				false);
		final String paramMissing1 = validator.isValid("", null, null, null, null, null, null, null, null, false,
				false);
		final String paramMissing2 = validator.isValid(null, "", null, null, null, null, null, null, null, false,
				false);
		final String paramMissing3 = validator.isValid(null, null, "", null, null, null, null, null, null, false,
				false);
		final String paramMissing4 = validator.isValid("", "", null, null, null, null, null, null, null, false, false);
		final String paramMissing5 = validator.isValid("", null, "", null, null, null, null, null, null, false, false);
		final String paramMissing6 = validator.isValid(null, "", "", null, null, null, null, null, null, false, false);
		final String paramMissing7 = validator.isValid("", "", "", null, null, null, null, null, null, false, false);
		final String paramMissing8 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "", "", null, null, null,
				null, null, null, false, false);
		final String paramMissing9 = validator.isValid("http://eng-hub-valid01.dc1.lan/", null, "", null, null, null,
				null, null, null, false, false);
		final String paramMissing10 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "", null, null, null, null,
				null, null, null, false, false);
		final String paramMissing11 = validator.isValid("http://eng-hub-valid01.dc1.lan/", null, null, null, null, null,
				null, null, null, false, false);
		assertEquals(nullTestString, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing1, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing2, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing3, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing4, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing5, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing6, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing7, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing8, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing9, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing10, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing11, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
	}

	@Test
	public void testUseOfDefaultTimeout() {
		validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null, null, null, null, null,
				null, true, false);
		assertNotNull(validator.getTimeout());
		assertEquals("120", validator.getTimeout());
		validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null, null, null, null, null,
				null, false, false);
		assertNotNull(validator.getTimeout());
		assertEquals("120", validator.getTimeout());
		validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null, null, null, null, null,
				"300", false, false);
		assertNotNull(validator.getTimeout());
		assertEquals("300", validator.getTimeout());
		validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null, null, null, null, null,
				"300", true, false);
		assertNotNull(validator.getTimeout());
		assertEquals("120", validator.getTimeout());
	}
}
