package com.blackducksoftware.integration.eclipseplugin.internal.listeners;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.build.utils.FilePathGavExtractor;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ClasspathVariables;
import com.blackducksoftware.integration.eclipseplugin.common.services.DependencyInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;

public class ProjectDependenciesChangedListener implements IElementChangedListener {
	private final ProjectDependencyInformation information;
	private String projectName;
	private final FilePathGavExtractor extractor;
	private final DependencyInformationService depService;

	public ProjectDependenciesChangedListener(final ProjectDependencyInformation information,
			final FilePathGavExtractor extractor, final DependencyInformationService depService) {
		this.information = information;
		this.extractor = extractor;
		this.depService = depService;
	}

	@Override
	public void elementChanged(final ElementChangedEvent event) {
		visit(event.getDelta());
	}

	private void visit(final IJavaElementDelta delta) {
		final IJavaElement el = delta.getElement();
		switch (el.getElementType()) {
		case IJavaElement.JAVA_MODEL: {
			visitChildren(delta);
			break;
		}
		case IJavaElement.JAVA_PROJECT: {
			if (isClasspathChanged(delta.getFlags())) {
				projectName = el.getElementName();
				visitChildren(delta);
			}
			break;
		}
		case IJavaElement.PACKAGE_FRAGMENT_ROOT: {
			final String OSSpecificFilepath = el.getPath().toOSString();
			if (delta.getKind() == IJavaElementDelta.ADDED || delta.getKind() == IJavaElementDelta.CHANGED) {
				if (depService.isMavenDependency(OSSpecificFilepath)) {
					final IPath mavenPath = JavaCore.getClasspathVariable(ClasspathVariables.MAVEN);
					if (mavenPath != null) {
						final String mavenPathString = mavenPath.toOSString();
						final Gav gav = extractor.getMavenPathGav(OSSpecificFilepath, mavenPathString);
						information.addWarningToProject(projectName, gav);
						information.printAllInfo();
					}
				} else if (depService.isGradleDependency(OSSpecificFilepath)) {
					final Gav gav = extractor.getGradlePathGav(OSSpecificFilepath);
					information.addWarningToProject(projectName, gav);
					information.printAllInfo();
				}
			} else if (delta.getKind() == IJavaElementDelta.REMOVED) {
				if (depService.isMavenDependency(OSSpecificFilepath)) {
					final IPath mavenPath = JavaCore.getClasspathVariable(ClasspathVariables.MAVEN);
					if (mavenPath != null) {
						final String mavenPathString = mavenPath.toOSString();
						final Gav gav = extractor.getMavenPathGav(OSSpecificFilepath, mavenPathString);
						information.removeWarningFromProject(projectName, gav);
						information.printAllInfo();
					}
				} else if (depService.isGradleDependency(OSSpecificFilepath)) {
					final Gav gav = extractor.getGradlePathGav(OSSpecificFilepath);
					information.removeWarningFromProject(projectName, gav);
					information.printAllInfo();
				}
			}
			break;
		}
		default: {
			break;
		}
		}
	}

	private boolean isClasspathChanged(final int flags) {
		return 0 != (flags & (IJavaElementDelta.F_CLASSPATH_CHANGED | IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED));
	}

	public void visitChildren(final IJavaElementDelta delta) {
		for (final IJavaElementDelta c : delta.getAffectedChildren()) {
			visit(c);
		}
	}
}
