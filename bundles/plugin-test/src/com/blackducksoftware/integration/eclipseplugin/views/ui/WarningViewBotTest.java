package com.blackducksoftware.integration.eclipseplugin.views.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.blackducksoftware.integration.eclipseplugin.common.constants.MenuLabels;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewIds;
import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewNames;

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
	public void placeholderTest() {
		assertTrue(true);
	}

	@Ignore
	public void testThatWarningViewOpensFromContextMenu() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		final SWTBotMenu warningViewMenu = node.select().contextMenu(MenuLabels.BLACK_DUCK)
				.contextMenu(MenuLabels.WARNING_VIEW);
		warningViewMenu.click();
		assertNotNull(bot.viewByTitle(ViewNames.WARNING));
		assertTrue(bot.viewById(ViewIds.WARNING).isActive());
		bot.viewByTitle(ViewNames.WARNING).close();
	}

	@Ignore
	public void testThatWarningViewOpensFromWindowMenu() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.waitUntil(Conditions.shellIsActive("Show View"));
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
		bot.viewByTitle(ViewNames.WARNING).close();
	}

	@AfterClass
	public static void tearDownWorkspaceBot() {
		bot.resetWorkbench();
	}

}
