package com.blackducksoftware.integration.eclipseplugin.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.eclipseplugin.internal.ProjectDependencyInformation;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.google.common.cache.LoadingCache;

@RunWith(MockitoJUnitRunner.class)
public class ProjectDependencyInformationTest {

	private final String proj = "proj";

	@Mock
	Gav gav1, gav2, gav3;

	@Mock
	Warning warning1, warning2, warning3;

	@Mock
	LoadingCache<Gav, Warning> cache;

	@Mock
	ProjectInformationService projService;

	private void prepareCache() throws ExecutionException {
		Mockito.when(cache.get(gav1)).thenReturn(warning1);
		Mockito.when(cache.get(gav2)).thenReturn(warning2);
		Mockito.when(cache.get(gav3)).thenReturn(warning3);
	}

	private boolean containsGav(final Gav[] gavs, final Gav gav) {
		for (final Gav curGav : gavs) {
			if (curGav.equals(gav)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void testAddingProject() throws ExecutionException {
		prepareCache();
		Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new Gav[] { gav1, gav2, gav3 });
		final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, cache);
		projInfo.addProject(proj);
		final Gav[] gavs = projInfo.getAllDependencyGavs(proj);
		assertTrue(containsGav(gavs, gav1));
		assertTrue(containsGav(gavs, gav2));
		assertTrue(containsGav(gavs, gav3));
	}

	@Test
	public void testAddingDependency() throws ExecutionException {
		prepareCache();
		Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new Gav[] { gav1, gav2 });
		final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, cache);
		projInfo.addProject(proj);
		assertTrue(projInfo.containsProject(proj));
		final Gav[] gavsBefore = projInfo.getAllDependencyGavs(proj);
		assertFalse(containsGav(gavsBefore, gav3));
		projInfo.addWarningToProject(proj, gav3);
		final Gav[] gavsAfter = projInfo.getAllDependencyGavs(proj);
		assertTrue(containsGav(gavsAfter, gav3));
	}

	@Test
	public void testRemovingDependency() throws ExecutionException {
		prepareCache();
		Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new Gav[] { gav1, gav2, gav3 });
		final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, cache);
		projInfo.addProject(proj);
		final Gav[] gavsBefore = projInfo.getAllDependencyGavs(proj);
		assertTrue(containsGav(gavsBefore, gav3));
		projInfo.removeWarningFromProject(proj, gav3);
		final Gav[] gavsAfter = projInfo.getAllDependencyGavs(proj);
		assertFalse(containsGav(gavsAfter, gav3));
	}

	@Test
	public void testRemovingProject() throws ExecutionException {
		prepareCache();
		Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new Gav[] { gav1, gav2 });
		final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, cache);
		projInfo.addProject(proj);
		assertTrue(projInfo.containsProject(proj));
		projInfo.removeProject(proj);
		assertFalse(projInfo.containsProject(proj));
	}
}
