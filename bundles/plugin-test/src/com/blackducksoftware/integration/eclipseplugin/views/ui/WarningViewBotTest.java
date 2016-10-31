package com.blackducksoftware.integration.eclipseplugin.views.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.blackducksoftware.integration.eclipseplugin.common.constants.MenuLabels;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewIds;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewNames;
import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

@RunWith(SWTBotJunit4ClassRunner.class)
public class WarningViewBotTest {

	public static SWTWorkbenchBot bot;

	private static final String TEST_PROJECT_NAME = "warning-view-test-project";

	@BeforeClass
	public static void setUpWorkspaceBot() {
		bot = new SWTWorkbenchBot();
		try {
			bot.viewByTitle("Welcome").close();
		} catch (final RuntimeException e) {
		}
		final SWTBotMenu fileMenu = bot.menu("File");
		final SWTBotMenu projectMenu = fileMenu.menu("New");
		final SWTBotMenu newMenu = projectMenu.menu("Project...");
		newMenu.click();
		bot.waitUntil(Conditions.shellIsActive("New Project"));
		final SWTBotTree optionTree = bot.tree();
		final SWTBotTreeItem javaNode = optionTree.expandNode("Java");
		bot.waitUntil(new DefaultCondition() {
			@Override
			public String getFailureMessage() {
				return "Java Project node unavailable";
			}

			@Override
			public boolean test() throws Exception {
				return javaNode.isExpanded();
			}
		});
		javaNode.expandNode("Java Project").select();
		bot.waitUntil(new DefaultCondition() {
			@Override
			public String getFailureMessage() {
				return "unable to select Next button";
			}

			@Override
			public boolean test() throws Exception {
				return bot.button("Next >").isEnabled();
			}

		});
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(TEST_PROJECT_NAME);
		bot.button("Finish").click();
		try {
			bot.waitUntil(Conditions.shellIsActive("Open Associated Perspective?"));
			bot.button("Yes").click();
		} catch (final TimeoutException e) {
		}
	}

	@Test
	public void testThatWarningViewOpensFromContextMenu() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		final SWTBotMenu blackDuckMenu = node.select().contextMenu(MenuLabels.BLACK_DUCK);
		final SWTBotMenu warningViewMenu = blackDuckMenu.contextMenu(MenuLabels.WARNING_VIEW);
		warningViewMenu.click();
		assertNotNull(bot.viewByTitle(ViewNames.WARNING));
		assertTrue(bot.viewByTitle(ViewNames.WARNING).isActive());
		assertNotNull(bot.viewById(ViewIds.WARNING));
		assertTrue(bot.viewById(ViewIds.WARNING).isActive());
		bot.viewByTitle(ViewNames.WARNING).close();
	}

	@Test
	public void testThatWarningViewOpensFromWindowMenu() {
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
		assertNotNull(bot.viewByTitle(ViewNames.WARNING));
		assertTrue(bot.viewByTitle(ViewNames.WARNING).isActive());
		assertNotNull(bot.viewById(ViewIds.WARNING));
		assertTrue(bot.viewById(ViewIds.WARNING).isActive());
		bot.viewByTitle(ViewNames.WARNING).close();
	}

	@Test
	public void testWarningViewLabels() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		final SWTBotMenu blackDuckMenu = node.select().contextMenu(MenuLabels.BLACK_DUCK);
		final SWTBotMenu warningViewMenu = blackDuckMenu.contextMenu(MenuLabels.WARNING_VIEW);
		warningViewMenu.click();
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
	}

	@Test
	public void testColumnContents() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		final SWTBotMenu blackDuckMenu = node.select().contextMenu(MenuLabels.BLACK_DUCK);
		final SWTBotMenu warningViewMenu = blackDuckMenu.contextMenu(MenuLabels.WARNING_VIEW);
		warningViewMenu.click();
		final SWTBotView warningView = bot.viewByTitle(ViewNames.WARNING);
		node.click();
		System.out.println(warningView.bot().table().rowCount());
	}

	@AfterClass
	public static void tearDownWorkspaceBot() {
		bot.resetWorkbench();
	}

}
