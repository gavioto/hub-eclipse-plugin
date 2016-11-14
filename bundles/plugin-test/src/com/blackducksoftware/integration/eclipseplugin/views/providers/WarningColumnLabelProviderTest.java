package com.blackducksoftware.integration.eclipseplugin.views.providers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.internal.Vulnerability;

@RunWith(MockitoJUnitRunner.class)
public class WarningColumnLabelProviderTest {

    @Mock
    Vulnerability vulnerability;

    private final String VULNERABILITY_NAME = "vulnerability name";

    private final String DESCRIPTION = "description";

    private final double BASE_SCORE = 0;

    private final String SEVERITY = "severity";

    private void setUpVulnerability() {
        Mockito.when(vulnerability.getVulnerabilityName()).thenReturn(VULNERABILITY_NAME);
        Mockito.when(vulnerability.getDescription()).thenReturn(DESCRIPTION);
        Mockito.when(vulnerability.getBaseScore()).thenReturn(BASE_SCORE);
        Mockito.when(vulnerability.getSeverity()).thenReturn(SEVERITY);
    }

    @Test
    public void testVulnerabilityNameColumnIndex() {
        setUpVulnerability();
        WarningColumnLabelProvider provider = new WarningColumnLabelProvider(WarningColumnLabelProvider.VULNERABILITY_NAME_COLUMN_INDEX);
        String text = provider.getText(vulnerability);
        assertEquals(VULNERABILITY_NAME, text);
    }

    @Test
    public void testDescriptionColumnIndex() {
        setUpVulnerability();
        WarningColumnLabelProvider provider = new WarningColumnLabelProvider(WarningColumnLabelProvider.VULNERABILITY_DESCRIPTION_COLUMN_INDEX);
        String text = provider.getText(vulnerability);
        assertEquals(DESCRIPTION, text);
    }

    @Test
    public void testBaseScoreColumnIndex() {
        setUpVulnerability();
        WarningColumnLabelProvider provider = new WarningColumnLabelProvider(WarningColumnLabelProvider.VULNERABILITY_BASE_SCORE_COLUMN_INDEX);
        String text = provider.getText(vulnerability);
        assertEquals(Double.toString(BASE_SCORE), text);
    }

    @Test
    public void testSeverityColumnIndex() {
        setUpVulnerability();
        WarningColumnLabelProvider provider = new WarningColumnLabelProvider(WarningColumnLabelProvider.VULNERABILITY_SEVERITY_COLUMN_INDEX);
        String text = provider.getText(vulnerability);
        assertEquals(SEVERITY, text);
    }

}
