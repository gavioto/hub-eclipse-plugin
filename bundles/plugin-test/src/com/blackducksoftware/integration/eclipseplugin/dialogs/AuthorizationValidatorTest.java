package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AuthorizationValidatorTest {
	
	private final AuthorizationValidator validator = new AuthorizationValidator();
	
	@Test
	public void testHubAuthorizationFailure() {
		String badUsername = validator.isValid("http://eng-hub-valid01.dc1.lan/", "fake", "blackduck", null, null, null, null, null, null, false, false);
		String badPassword = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "fake", null, null, null, null, null, null, false, false);
		String badUsernameAndPassword = validator.isValid("http://eng-hub-valid01.dc1.lan/", "f", "f", null, null, null, null, null, null, false, false);
		assertEquals(badUsername, AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE);
		assertEquals(badPassword, AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE);
		assertEquals(badUsernameAndPassword, AuthorizationDialog.INCORRECT_CREDENTIALS_MESSAGE);
	}
	
	@Test
	public void testProxyCredentialValidation() {
		String validCredentials1 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", null, null, null, null, null, null, false, true);
		String validCredentials2 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", "p", null, null, null, null, null, false, true);
		String validCredentials3 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "sysadmin", "blackduck", "p", null, null, null, null, null, false, false);
		assertEquals(validCredentials1, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(validCredentials2, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(validCredentials1, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
		assertEquals(validCredentials3, AuthorizationDialog.LOGIN_SUCCESS_MESSAGE);
	}
	
	@Test
	public void testHubAuthorizationSuccess() {
	}

	@Test
	public void testRequiredParamMissingOutput() {
		String nullTestString = validator.isValid(null, null, null, null, null, null, null, null, null, false, false);
		String paramMissing1 = validator.isValid("", null, null, null, null, null, null, null, null, false, false);
		String paramMissing2 = validator.isValid(null, "", null, null, null, null, null, null, null, false, false);
		String paramMissing3 = validator.isValid(null, null, "", null, null, null, null, null, null, false, false);
		String paramMissing4 = validator.isValid("", "", null, null, null, null, null, null, null, false, false);
		String paramMissing5 = validator.isValid("", null, "", null, null, null, null, null, null, false, false);
		String paramMissing6 = validator.isValid(null, "", "", null, null, null, null, null, null, false, false);
		String paramMissing7 = validator.isValid("", "", "", null, null, null, null, null, null, false, false);
		String paramMissing8 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "", "", null, null, null, null, null, null, false, false);
		String paramMissing9 = validator.isValid("http://eng-hub-valid01.dc1.lan/", null, "", null, null, null, null, null, null, false, false);
		String paramMissing10 = validator.isValid("http://eng-hub-valid01.dc1.lan/", "", null, null, null, null, null, null, null, false, false);
		String paramMissing11 = validator.isValid("http://eng-hub-valid01.dc1.lan/", null, null, null, null, null, null, null, null, false, false);
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
}
