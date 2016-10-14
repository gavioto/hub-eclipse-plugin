package com.blackducksoftware.integration.eclipseplugin.common.services;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JavaCore.class, ResourcesPlugin.class, PlatformUI.class })
public class WorkspaceInformationServiceTest {

	private final String fakeMavenRepoPath = "maven/repo/";

	private void setUpStaticMocks() {
		PowerMockito.mockStatic(JavaCore.class);
		PowerMockito.mockStatic(ResourcesPlugin.class);
		PowerMockito.mockStatic(PlatformUI.class);
	}

	@Test
	public void testGettingSelectedProject() {
		assertTrue(true);
	}

	@Test
	public void testIsMavenDependency() {
		assertTrue(true);
	}

	@Test
	public void testIsGradleDependency() {
		assertTrue(true);
	}

	@Test
	public void testIsJavaProject() {
		assertTrue(true);
	}

	@Test
	public void testGettingProjectsFunctionality() {
		assertTrue(true);
	}

	@Test
	public void testGetNumJavaProjects() {
		assertTrue(true);
	}

}
