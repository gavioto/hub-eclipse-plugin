package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

public class WarningLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		if (element instanceof Warning) {
			if (columnIndex == Warning.COMPONENT_COLUMN_INDEX) {
				return "Column";
			} else if (columnIndex == Warning.LICENSE_COLUMN_INDEX) {
				return "License";
			} else if (columnIndex == Warning.MATCH_COUNT_COLUMN_INDEX) {
				return "Match Count";
			} else if (columnIndex == Warning.MATCH_TYPE_COLUMN_INDEX) {
				return "Match Type";
			} else if (columnIndex == Warning.OPERATIONAL_RISK_COLUMN_INDEX) {
				return "Operational Risk";
			} else if (columnIndex == Warning.SECURITY_RISK_COLUMN_INDEX) {
				return "Security Risk";
			} else if (columnIndex == Warning.USAGE_COLUMN_INDEX) {
				return "Usage";
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
