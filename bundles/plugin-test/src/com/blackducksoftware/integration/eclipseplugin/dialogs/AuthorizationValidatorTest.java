package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AuthorizationValidatorTest {

	@Test
	public void validateOutputs() {
		AuthorizationValidator validator = new AuthorizationValidator();
		String nullTestString = validator.isValid(null, null, null, null, null, null, null, null, null, false, false);
		String paramMissing1 = validator.isValid("", null, null, null, null, null, null, null, null, false, false);
		String paramMissing2 = validator.isValid(null, "", null, null, null, null, null, null, null, false, false);
		String paramMissing3 = validator.isValid(null, null, "", null, null, null, null, null, null, false, false);
		String paramMissing4 = validator.isValid("", "", null, null, null, null, null, null, null, false, false);
		String paramMissing5 = validator.isValid("", null, "", null, null, null, null, null, null, false, false);
		String paramMissing6 = validator.isValid(null, "", "", null, null, null, null, null, null, false, false);
		String paramMissing7 = validator.isValid("", "", "", null, null, null, null, null, null, false, false);
		assertEquals(nullTestString, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing1, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing2, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing3, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing4, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing5, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing6, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
		assertEquals(paramMissing7, AuthorizationDialog.CREDENTIAL_MISSING_MESSAGE);
	}
}
