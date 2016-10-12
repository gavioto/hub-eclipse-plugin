package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.blackducksoftware.integration.eclipseplugin.common.services.WorkspaceInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.popupmenu.Activator;

/*
 * Class that provides the content for the warning view
 */
public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(final Object projectName) {

		if (projectName.equals("")) {
			final String[] noSelectedProject = { "No project currently selected" };
			return noSelectedProject;

		} else if (projectName instanceof String) {

			// filter what warnings are displayed based on user preferences
			final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
			final boolean isActivated = prefs.getBoolean((String) projectName);
			final String displayWarningsPropertyId = StringUtils
					.join(new String[] { (String) projectName, "displayWarnings" }, ':');
			final boolean displayWarnings = prefs.getBoolean(displayWarningsPropertyId);
			if (isActivated && displayWarnings) {
				String[] dependencies;
				try {
					dependencies = WorkspaceInformationService.getMavenAndGradleDependencies((String) projectName);
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
