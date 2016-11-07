package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

public class WarningContentProvider extends ArrayContentProvider implements IStructuredContentProvider {

	private final IPreferenceStore prefs;
	private final ProjectDependencyInformation information;

	public static final String[] NO_SELECTED_PROJECT = new String[] { "No open project currently selected" };
	public static final String[] PROJECT_NOT_ACTIVATED = new String[] {
			"Black Duck scan not activated for current project" };
	public static final String[] ERR_UNKNOWN_INPUT = new String[] { "Input is of unknown type" };

	public WarningContentProvider(final IPreferenceStore prefs, final ProjectDependencyInformation information) {
		super();
		this.prefs = prefs;
		this.information = information;
	}

	@Override
	public Object[] getElements(final Object projectName) {

		if (projectName.equals("")) {
			return NO_SELECTED_PROJECT;

		} else if (projectName instanceof String) {

			final boolean isActivated = prefs.getBoolean((String) projectName);
			if (isActivated) {
				final Gav[] gavs = information.getAllDependencyGavs((String) projectName);
				final String[] messages = new String[gavs.length];
				for (int i = 0; i < gavs.length; i++) {
					messages[i] = getGavMessage(gavs[i]);
				}
				return messages;
			} else {
				return PROJECT_NOT_ACTIVATED;
			}

		} else {
			return ERR_UNKNOWN_INPUT;
		}
	}

	public String getGavMessage(final Gav gav) {
		final String[] elements = new String[] { "group: " + gav.getGroupId(), "artifact: " + gav.getArtifactId(),
				"version: " + gav.getVersion() };
		return StringUtils.join(elements, ", ");
	}

}
