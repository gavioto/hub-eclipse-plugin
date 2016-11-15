package com.blackducksoftware.integration.eclipseplugin.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.build.Gav;
import com.blackducksoftware.integration.build.GavWithType;
import com.blackducksoftware.integration.eclipseplugin.common.services.ProjectInformationService;
import com.blackducksoftware.integration.hub.api.vulnerabilities.VulnerabilityItem;
import com.google.common.cache.LoadingCache;

@RunWith(MockitoJUnitRunner.class)
public class ProjectDependencyInformationTest {

    private final String proj = "proj";

    @Mock
    Gav gav1, gav2, gav3;

    @Mock
    GavWithType gavWithType1, gavWithType2, gavWithType3;

    @Mock
    List<VulnerabilityItem> vulnerabilities1, vulnerabilities2, vulnerabilities3;

    @Mock
    LoadingCache<GavWithType, List<VulnerabilityItem>> cache;

    @Mock
    ComponentCache componentCache;

    @Mock
    ProjectInformationService projService;

    private void prepareCache() throws ExecutionException {
        Mockito.when(componentCache.getCache()).thenReturn(cache);
        Mockito.when(gavWithType1.getGav()).thenReturn(gav1);
        Mockito.when(gavWithType2.getGav()).thenReturn(gav2);
        Mockito.when(gavWithType3.getGav()).thenReturn(gav3);
        Mockito.when(cache.get(gavWithType1)).thenReturn(vulnerabilities1);
        Mockito.when(cache.get(gavWithType2)).thenReturn(vulnerabilities2);
        Mockito.when(cache.get(gavWithType3)).thenReturn(vulnerabilities3);
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
        Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new GavWithType[] { gavWithType1, gavWithType2, gavWithType3 });
        final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, componentCache);
        projInfo.addProject(proj);
        final Gav[] gavs = projInfo.getAllDependencyGavs(proj);
        assertTrue(containsGav(gavs, gav1));
        assertTrue(containsGav(gavs, gav2));
        assertTrue(containsGav(gavs, gav3));
    }

    @Test
    public void testAddingDependency() throws ExecutionException {
        prepareCache();
        Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new GavWithType[] { gavWithType1, gavWithType2 });
        final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, componentCache);
        projInfo.addProject(proj);
        assertTrue(projInfo.containsProject(proj));
        final Gav[] gavsBefore = projInfo.getAllDependencyGavs(proj);
        assertFalse(containsGav(gavsBefore, gav3));
        projInfo.addWarningToProject(proj, gavWithType3);
        final Gav[] gavsAfter = projInfo.getAllDependencyGavs(proj);
        assertTrue(containsGav(gavsAfter, gav3));
    }

    @Test
    public void testRemovingDependency() throws ExecutionException {
        prepareCache();
        Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new GavWithType[] { gavWithType1, gavWithType2, gavWithType3 });
        final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, componentCache);
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
        Mockito.when(projService.getMavenAndGradleDependencies(proj)).thenReturn(new GavWithType[] { gavWithType1, gavWithType2 });
        final ProjectDependencyInformation projInfo = new ProjectDependencyInformation(projService, componentCache);
        projInfo.addProject(proj);
        assertTrue(projInfo.containsProject(proj));
        projInfo.removeProject(proj);
        assertFalse(projInfo.containsProject(proj));
    }
}
