package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

public class WarningColumnLabelProvider extends ColumnLabelProvider {

	private final int columnIndex;

	public WarningColumnLabelProvider(final int columnIndex) {
		super();
		this.columnIndex = columnIndex;
	}

	@Override
	public String getText(final Object warning) {
		if (warning instanceof Warning) {
			if (columnIndex == Warning.COMPONENT_COLUMN_INDEX) {
				return ((Warning) warning).getComponent();
			} else if (columnIndex == Warning.LICENSE_COLUMN_INDEX) {
				return ((Warning) warning).getLicense();
			} else if (columnIndex == Warning.MATCH_COUNT_COLUMN_INDEX) {
				return ((Warning) warning).getMatchCount();
			} else if (columnIndex == Warning.MATCH_TYPE_COLUMN_INDEX) {
				return ((Warning) warning).getMatchType();
			} else if (columnIndex == Warning.OPERATIONAL_RISK_COLUMN_INDEX) {
				return ((Warning) warning).getOperationalRisk();
			} else if (columnIndex == Warning.SECURITY_RISK_COLUMN_INDEX) {
				return ((Warning) warning).getSecurityRisk();
			} else if (columnIndex == Warning.USAGE_COLUMN_INDEX) {
				return ((Warning) warning).getUsage();
			} else {
				return "";
			}
		} else if (warning instanceof String && columnIndex == 0) {
			return (String) warning;
		} else {
			return "";
		}
	}
}
