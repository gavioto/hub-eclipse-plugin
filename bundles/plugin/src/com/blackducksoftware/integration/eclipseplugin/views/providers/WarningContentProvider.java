package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ProjectSpecificPreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

/*
 * Class that provides the content for the warning view
 */
public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	private final IPreferenceStore prefs;

	public WarningContentProvider(final IPreferenceStore prefs) {
		super();
		this.prefs = prefs;
	}

	@Override
	public Object[] getElements(final Object projectName) {

		if (projectName.equals("")) {
			final String[] noSelectedProject = { "No project currently selected" };
			return noSelectedProject;

		} else if (projectName instanceof String) {

			// filter what warnings are displayed based on user preferences
			final boolean isActivated = prefs.getBoolean((String) projectName);
			final String displayWarningsPropertyId = StringUtils
					.join(new String[] { (String) projectName, ProjectSpecificPreferenceNames.ACTIVATE_PROJECT }, ':');
			final boolean displayWarnings = prefs.getBoolean(displayWarningsPropertyId);
			if (isActivated && displayWarnings) {
				String[] dependencies;
				try {
					final DependencyInformationService depService = new DependencyInformationService();
					final FilePathGavExtractor extractor = new FilePathGavExtractor();
					final ProjectInformationService projService = new ProjectInformationService(depService, extractor);
					dependencies = projService.getMavenAndGradleDependencies((String) projectName);
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
