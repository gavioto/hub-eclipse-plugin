package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectInfoProvider;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
// import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

// eventually, instead of just strings, this will return an array of warnings (once hub REST API is established)
public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(final Object projectName) {
		if (projectName instanceof String) {
			final String[] dependencies = ProjectInfoProvider.getDependencies((String) projectName);
			final Warning[] warnings = new Warning[dependencies.length];
			for (int i = 0; i < dependencies.length; i++) {
				warnings[i] = new Warning(dependencies[i], 0, "", "", "", "", "");
			}
			return warnings;
		} else {
			return new Warning[0];
		}
	}

}
