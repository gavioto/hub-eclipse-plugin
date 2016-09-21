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
				return ((Warning) element).getComponent();
			} else if (columnIndex == Warning.LICENSE_COLUMN_INDEX) {
				return ((Warning) element).getLicense();
			} else if (columnIndex == Warning.MATCH_COUNT_COLUMN_INDEX) {
				return ((Warning) element).getMatchCount();
			} else if (columnIndex == Warning.MATCH_TYPE_COLUMN_INDEX) {
				return ((Warning) element).getMatchType();
			} else if (columnIndex == Warning.OPERATIONAL_RISK_COLUMN_INDEX) {
				return ((Warning) element).getOperationalRisk();
			} else if (columnIndex == Warning.SECURITY_RISK_COLUMN_INDEX) {
				return ((Warning) element).getSecurityRisk();
			} else if (columnIndex == Warning.USAGE_COLUMN_INDEX) {
				return ((Warning) element).getUsage();
			} else {
				return "";
			}
		} else if (element instanceof String) {
			if (columnIndex == 0) {
				return (String) element;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
}
