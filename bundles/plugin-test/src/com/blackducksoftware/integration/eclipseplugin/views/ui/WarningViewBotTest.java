package com.blackducksoftware.integration.eclipseplugin.views.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;
import com.blackducksoftware.integration.eclipseplugin.test.utils.SWTBotUtils;
import com.blackducksoftware.integration.eclipseplugin.views.providers.WarningContentProvider;

@RunWith(SWTBotJunit4ClassRunner.class)
public class WarningViewBotTest {

	private static SWTWorkbenchBot bot;
	private static SWTBotUtils botUtils;

	private static final String TEST_JAVA_PROJECT_NAME = "warning-view-test-java-project";
	private static final String TEST_NON_JAVA_PROJECT_NAME = "warning-view-test-non-java-project";

	@BeforeClass
	public static void setUpWorkspaceBot() {
		bot = new SWTWorkbenchBot();
		botUtils = new SWTBotUtils();
		try {
			bot.viewByTitle("Welcome").close();
		} catch (final RuntimeException e) {
		}
		botUtils.createJavaProject(TEST_JAVA_PROJECT_NAME, bot);
		botUtils.createNonJavaProject(TEST_NON_JAVA_PROJECT_NAME, bot);
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
			if (i == Warning.COMPONENT_COLUMN_INDEX) {
				assertEquals("Component", label);
			}
			if (i == Warning.LICENSE_COLUMN_INDEX) {
				assertEquals("License", label);
			}
			if (i == Warning.MATCH_COUNT_COLUMN_INDEX) {
				assertEquals("Match Count", label);
			}
			if (i == Warning.MATCH_TYPE_COLUMN_INDEX) {
				assertEquals("Match Type", label);
			}
			if (i == Warning.OPERATIONAL_RISK_COLUMN_INDEX) {
				assertEquals("Operational Risk", label);
			}
			if (i == Warning.SECURITY_RISK_COLUMN_INDEX) {
				assertEquals("Security Risk", label);
			}
			if (i == Warning.USAGE_COLUMN_INDEX) {
				assertEquals("Usage", label);
			}
			i++;
		}
		bot.viewByTitle(ViewNames.WARNING).close();
	}

	@Test
	public void testWhenProjectDeleted() {
		botUtils.deleteProjectFromDisk(TEST_NON_JAVA_PROJECT_NAME, bot);
		openWarningViewFromWindowMenu();
		final SWTBotView warningView = bot.viewByTitle(ViewNames.WARNING);
		assertEquals(WarningContentProvider.NO_SELECTED_PROJECT[0], warningView.bot().table().cell(0, 0));
		bot.viewByTitle(ViewNames.WARNING).close();
		botUtils.createNonJavaProject(TEST_NON_JAVA_PROJECT_NAME, bot);
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

	@AfterClass
	public static void tearDownWorkspaceBot() {
		bot.resetWorkbench();
	}

}
