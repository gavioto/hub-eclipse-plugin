package com.blackducksoftware.integration.eclipseplugin.views.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.blackducksoftware.integration.eclipseplugin.common.constants.MenuLabels;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewIds;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewNames;
import com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot.SWTBotProjectUtils;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningColumnLabelProvider;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

@RunWith(SWTBotJunit4ClassRunner.class)
public class WarningViewBotTest {

    private static SWTWorkbenchBot bot;

    private static SWTBotProjectUtils botUtils;

    private static final String TEST_JAVA_PROJECT_NAME = "warning-view-test-java-project";

    private static final String TEST_NON_JAVA_PROJECT_NAME = "warning-view-test-non-java-project";

    private static final String TEST_MAVEN_PROJECT_GROUP_ID = "test.group.id";

    private static final String TEST_MAVEN_PROJECT_ARTIFACT_ID = "test.artifact.id";

    private static final String[][] TEST_GAVS = new String[][] { new String[] { "junit", "junit", "4.12" },
            new String[] { "org.hamcrest", "hamcrest-core", "1.3" },
            new String[] { "org.mockito", "mockito-all", "1.10.19" },
            new String[] { "nl.jqno.equalsverifier", "equalsverifier", "2.1.6" } };

    @BeforeClass
    public static void setUpWorkspaceBot() {
        bot = new SWTWorkbenchBot();
        botUtils = new SWTBotProjectUtils(bot);
        try {
            bot.viewByTitle("Welcome").close();
        } catch (final RuntimeException e) {
        }
        botUtils.createJavaProject(TEST_JAVA_PROJECT_NAME);
        botUtils.createNonJavaProject(TEST_NON_JAVA_PROJECT_NAME);
    }

    private void openWarningViewFromContextMenu(final String projectName) {
        final SWTBotTreeItem javaProjectNode = bot.viewByTitle("Package Explorer").bot().tree()
                .getTreeItem(projectName);
        javaProjectNode.setFocus();
        final SWTBotMenu blackDuckMenu = javaProjectNode.select().contextMenu(MenuLabels.BLACK_DUCK);
        final SWTBotMenu warningViewMenu = blackDuckMenu.contextMenu(MenuLabels.WARNING_VIEW);
        warningViewMenu.click();
    }

    private void openWarningViewFromWindowMenu() {
        bot.menu("Window").menu("Show View").menu("Other...").click();
        bot.shell("Show View").activate();
        bot.tree().expandNode(ViewNames.BLACK_DUCK).expandNode(ViewNames.WARNING).select();
        bot.waitUntil(new DefaultCondition() {
            @Override
            public String getFailureMessage() {
                return "ok button could not be enabled";
            }

            @Override
            public boolean test() {
                return bot.button("OK").isEnabled();
            }
        });
        bot.button("OK").click();
    }

    @Test
    public void testThatWarningViewOpensFromContextMenu() {
        openWarningViewFromContextMenu(TEST_JAVA_PROJECT_NAME);
        assertNotNull(bot.viewByTitle(ViewNames.WARNING));
        assertTrue(bot.viewByTitle(ViewNames.WARNING).isActive());
        assertNotNull(bot.viewById(ViewIds.WARNING));
        assertTrue(bot.viewById(ViewIds.WARNING).isActive());
        bot.viewByTitle(ViewNames.WARNING).close();
    }

    @Test
    public void testThatWarningViewOpensFromWindowMenu() {
        openWarningViewFromWindowMenu();
        assertNotNull(bot.viewByTitle(ViewNames.WARNING));
        assertTrue(bot.viewByTitle(ViewNames.WARNING).isActive());
        assertNotNull(bot.viewById(ViewIds.WARNING));
        assertTrue(bot.viewById(ViewIds.WARNING).isActive());
        bot.viewByTitle(ViewNames.WARNING).close();
    }

    @Test
    public void testWarningViewLabels() {
        openWarningViewFromContextMenu(TEST_JAVA_PROJECT_NAME);
        final SWTBotView warningView = bot.viewByTitle(ViewNames.WARNING);
        final List<String> labels = warningView.bot().table().columns();
        int i = 0;
        final Iterator<String> it = labels.iterator();
        while (it.hasNext()) {
            final String label = it.next();
            if (i == WarningColumnLabelProvider.VULNERABILITY_NAME_COLUMN_INDEX) {
                assertEquals(WarningView.VULNERABILITY_NAME_LABEL, label);
            } else if (i == WarningColumnLabelProvider.VULNERABILITY_DESCRIPTION_COLUMN_INDEX) {
                assertEquals(WarningView.VULNERABILITY_DESCRIPTION_LABEL, label);
            } else if (i == WarningColumnLabelProvider.VULNERABILITY_BASE_SCORE_COLUMN_INDEX) {
                assertEquals(WarningView.VULNERABILITY_BASE_SCORE_LABEL, label);
            } else if (i == WarningColumnLabelProvider.VULNERABILITY_SEVERITY_COLUMN_INDEX) {
                assertEquals(WarningView.VULNERABILITY_SEVERITY_LABEL, label);
            } else {
                fail();
            }
            i++;
        }
        bot.viewByTitle(ViewNames.WARNING).close();
    }

    @Test
    public void testWhenProjectDeleted() {
        botUtils.deleteProjectFromDisk(TEST_NON_JAVA_PROJECT_NAME);
        openWarningViewFromWindowMenu();
        final SWTBotView warningView = bot.viewByTitle(ViewNames.WARNING);
        assertEquals(WarningContentProvider.NO_SELECTED_PROJECT[0], warningView.bot().table().cell(0, 0));
        bot.viewByTitle(ViewNames.WARNING).close();
        botUtils.createNonJavaProject(TEST_NON_JAVA_PROJECT_NAME);
    }

    @Test
    public void testForNonJavaProject() {
        openWarningViewFromWindowMenu();
        final SWTBotTreeItem nonJavaProjectNode = bot.viewByTitle("Package Explorer").bot().tree()
                .getTreeItem(TEST_NON_JAVA_PROJECT_NAME);
        nonJavaProjectNode.click();
        final SWTBotView warningView = bot.viewByTitle(ViewNames.WARNING);
        assertEquals(WarningContentProvider.PROJECT_NOT_ACTIVATED[0], warningView.bot().table().cell(0, 0));
        bot.viewById(ViewIds.WARNING).close();
    }

    @Test
    public void testDisplayingMavenProjectDependencies() {
        botUtils.createMavenProject(TEST_MAVEN_PROJECT_GROUP_ID, TEST_MAVEN_PROJECT_ARTIFACT_ID);
        for (final String[] gav : TEST_GAVS) {
            botUtils.addMavenDependency(TEST_MAVEN_PROJECT_ARTIFACT_ID, gav[0], gav[1], gav[2]);
        }
        botUtils.updateMavenProject(TEST_MAVEN_PROJECT_ARTIFACT_ID);
        openWarningViewFromContextMenu(TEST_MAVEN_PROJECT_ARTIFACT_ID);
        bot.sleep(10000);
        bot.viewById(ViewIds.WARNING).close();
    }

    @AfterClass
    public static void tearDownWorkspaceBot() {
        botUtils.deleteProjectFromDisk(TEST_JAVA_PROJECT_NAME);
        botUtils.deleteProjectFromDisk(TEST_MAVEN_PROJECT_ARTIFACT_ID);
        botUtils.deleteProjectFromDisk(TEST_NON_JAVA_PROJECT_NAME);
        bot.resetWorkbench();
    }

}
