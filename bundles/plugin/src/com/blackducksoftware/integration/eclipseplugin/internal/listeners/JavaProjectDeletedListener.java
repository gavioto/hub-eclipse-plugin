package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

public class JavaProjectDeletedListener implements IResourceChangeListener {

	private final ProjectDependencyInformation information;

	public JavaProjectDeletedListener(final ProjectDependencyInformation information) {
		this.information = information;
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event != null && event.getResource() != null) {
			information.removeProject(event.getResource().getName());
			information.printAllInfo();
		}
	}

}
