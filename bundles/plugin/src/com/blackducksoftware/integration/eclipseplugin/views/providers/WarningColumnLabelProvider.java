package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.blackducksoftware.integration.eclipseplugin.internal.Vulnerability;

public class WarningColumnLabelProvider extends ColumnLabelProvider {

    private final int columnIndex;

    public static final int VULNERABILITY_NAME_COLUMN_INDEX = 0;

    public static final int VULNERABILITY_DESCRIPTION_COLUMN_INDEX = 1;

    public static final int VULNERABILITY_BASE_SCORE_COLUMN_INDEX = 2;

    public static final int VULNERABILITY_SEVERITY_COLUMN_INDEX = 3;

    public WarningColumnLabelProvider(final int columnIndex) {
        super();
        this.columnIndex = columnIndex;
    }

    @Override
    public String getText(final Object input) {
        System.out.println("GETTING TEXT");
        if (input instanceof Vulnerability) {
            if (columnIndex == VULNERABILITY_NAME_COLUMN_INDEX) {
                return ((Vulnerability) input).getVulnerabilityName();
            } else if (columnIndex == VULNERABILITY_DESCRIPTION_COLUMN_INDEX) {
                return ((Vulnerability) input).getDescription();
            } else if (columnIndex == VULNERABILITY_BASE_SCORE_COLUMN_INDEX) {
                return Double.toString(((Vulnerability) input).getBaseScore());
            } else if (columnIndex == VULNERABILITY_SEVERITY_COLUMN_INDEX) {
                return ((Vulnerability) input).getSeverity();
            } else {
                return "";
            }
        } else if (input instanceof String && columnIndex == 0) {
            return (String) input;
        } else {
            return "";
        }
    }
}
