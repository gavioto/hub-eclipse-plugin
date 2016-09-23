package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectInfoProvider;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

// eventually, instead of just strings, this will return an array of warnings (once hub REST API is established)
public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(final Object projectName) {

		if (projectName.equals("")) {
			final String[] noSelectedProject = { "No project currently selected" };
			return noSelectedProject;

		} else if (projectName instanceof String) {
			final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
			final boolean isActivated = prefs.getBoolean((String) projectName);
			if (isActivated) {
				String[] dependencies;
				try {
					dependencies = ProjectInfoProvider.getMavenAndGradleDependencies((String) projectName);
					final Warning[] warnings = new Warning[dependencies.length];
					for (int i = 0; i < dependencies.length; i++) {

						// eventually this will call the REST API module instead
						// to construct the warning
						warnings[i] = new Warning(dependencies[i], 0, "", "", "", "", "");
					}
					return warnings;
				} catch (final CoreException e) {
					e.printStackTrace();
					return new String[] { "error occurred obtaining dependencies" };
				}
			} else {
				final String[] projectNotActivated = { "Black Duck scan not activated for current project" };
				return projectNotActivated;
			}

		} else {
			final String[] unknownInputType = { "input is of unknown type" };
			return unknownInputType;
		}
	}

}
