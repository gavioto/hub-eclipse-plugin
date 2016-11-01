package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	private final IPreferenceStore prefs;

	public static final String[] NO_SELECTED_PROJECT = new String[] { "No open project currently selected" };
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

			final boolean isActivated = prefs.getBoolean((String) projectName);
			if (isActivated) {
				String[] dependencies;
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
			} else {
				return PROJECT_NOT_ACTIVATED;
			}

		} else {
			return ERR_UNKNOWN_INPUT;
		}
	}

}
