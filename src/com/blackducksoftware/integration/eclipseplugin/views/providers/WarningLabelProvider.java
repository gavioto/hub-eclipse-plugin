package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.viewers.LabelProvider;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

public class WarningLabelProvider extends LabelProvider {
	
	public String getText(Object warning) {
		if (warning instanceof Warning) {
			return ((Warning) warning).getComponent();
		} else {
			return "unknown input type " + warning.getClass().toString();
		}
	}
}
