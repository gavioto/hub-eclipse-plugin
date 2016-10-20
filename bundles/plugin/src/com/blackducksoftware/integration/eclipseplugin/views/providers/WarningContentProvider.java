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

public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	private final IPreferenceStore prefs;

	public static final String[] NO_SELECTED_PROJECT = new String[] { "No project currently selected" };
	public static final String[] ERR_OBTAINING_DEPENDENCIES = new String[] { "Error occurred obtaining dependencies" };
	public static final String[] PROJECT_NOT_ACTIVATED = new String[] {
			"Black Duck scan not activated for current project" };
	public static final String[] ERR_UNKNOWN_INPUT = new String[] { "Input is of unknown type" };

	public WarningContentProvider(final IPreferenceStore prefs) {
		super();
		this.prefs = prefs;
	}

	@Override
	public Object[] getElements(final Object projectName) {

		if (projectName.equals("")) {
			return NO_SELECTED_PROJECT;

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
					return ERR_OBTAINING_DEPENDENCIES;
				}
			} else {
				return PROJECT_NOT_ACTIVATED;
			}

		} else {
			return ERR_UNKNOWN_INPUT;
		}
	}

}
