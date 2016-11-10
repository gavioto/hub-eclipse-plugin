package com.blackducksoftware.integration.eclipseplugin.views.providers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

@RunWith(MockitoJUnitRunner.class)
public class WarningColumnLabelProviderTest {

	@Mock
	Warning warning;

	private final String COMPONENT = "component";
	private final String LICENSE = "license";
	private final String MATCH_COUNT = "match count";
	private final String MATCH_TYPE = "match type";
	private final String OPERATIONAL_RISK = "operational risk";
	private final String SECURITY_RISK = "security risk";
	private final String USAGE = "usage";
	private final String MESSAGE = "message";

	private void setUpWarning() {
		Mockito.when(warning.getComponent()).thenReturn(COMPONENT);
		Mockito.when(warning.getLicense()).thenReturn(LICENSE);
		Mockito.when(warning.getMatchCount()).thenReturn(MATCH_COUNT);
		Mockito.when(warning.getMatchType()).thenReturn(MATCH_TYPE);
		Mockito.when(warning.getOperationalRisk()).thenReturn(OPERATIONAL_RISK);
		Mockito.when(warning.getSecurityRisk()).thenReturn(SECURITY_RISK);
		Mockito.when(warning.getUsage()).thenReturn(USAGE);
	}

	@Test
	public void testWarningAndComponentColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.COMPONENT_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(COMPONENT, text);
	}

	@Test
	public void testWarningAndLicenseColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.LICENSE_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(LICENSE, text);
	}

	@Test
	public void testWarningAndMatchCountColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.MATCH_COUNT_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(MATCH_COUNT, text);
	}

	@Test
	public void testWarningAndMatchTypeColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.MATCH_TYPE_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(MATCH_TYPE, text);
	}

	@Test
	public void testWarningAndOperationalRiskColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(
				Warning.OPERATIONAL_RISK_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(OPERATIONAL_RISK, text);
	}

	@Test
	public void testWarningAndSecurityRiskColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.SECURITY_RISK_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(SECURITY_RISK, text);
	}

	@Test
	public void testWarningAndUsageColumnIndex() {
		setUpWarning();
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.USAGE_COLUMN_INDEX);
		final String text = provider.getText(warning);
		assertEquals(USAGE, text);
	}

	@Test
	public void testStringAndComponentColumnIndex() {
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.COMPONENT_COLUMN_INDEX);
		final String text = provider.getText(MESSAGE);
		assertEquals(MESSAGE, text);
	}

	@Test
	public void testStringAndNonComponentColumnIndex() {
		final WarningColumnLabelProvider provider = new WarningColumnLabelProvider(Warning.USAGE_COLUMN_INDEX);
		final String text = provider.getText(MESSAGE);
		assertEquals("", text);
	}
}
