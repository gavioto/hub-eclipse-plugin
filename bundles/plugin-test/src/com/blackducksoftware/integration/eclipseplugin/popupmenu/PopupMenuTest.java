package com.blackducksoftware.integration.eclipseplugin.popupmenu;

import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.blackducksoftware.integration.eclipseplugin.common.constants.MenuLabels;

@RunWith(SWTBotJunit4ClassRunner.class)
public class PopupMenuTest {

	public static SWTWorkbenchBot bot;

	private static final String TEST_JAVA_PROJECT_NAME = "popup-menu-test-java-project";
	private static final String TEST_NON_JAVA_PROJECT_NAME = "popup-menu-test-non-java-project";

	@BeforeClass
	public static void setUpWorkspaceBot() {
		bot = new SWTWorkbenchBot();
		try {
			bot.viewByTitle("Welcome").close();
		} catch (final RuntimeException e) {
		}
		SWTBotMenu fileMenu = bot.menu("File");
		SWTBotMenu projectMenu = fileMenu.menu("New");
		SWTBotMenu newMenu = projectMenu.menu("Project...");
		newMenu.click();
		bot.waitUntil(Conditions.shellIsActive("New Project"));
		SWTBotTree optionTree = bot.tree();
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
		bot.textWithLabel("Project name:").setText(TEST_JAVA_PROJECT_NAME);
		bot.button("Finish").click();
		try {
			bot.waitUntil(Conditions.shellIsActive("Open Associated Perspective?"));
			bot.button("Yes").click();
		} catch (final TimeoutException e) {
		}
		fileMenu = bot.menu("File");
		projectMenu = fileMenu.menu("New");
		newMenu = projectMenu.menu("Project...");
		newMenu.click();
		bot.waitUntil(Conditions.shellIsActive("New Project"));
		optionTree = bot.tree();
		final SWTBotTreeItem generalNode = optionTree.expandNode("General");
		bot.waitUntil(new DefaultCondition() {
			@Override
			public String getFailureMessage() {
				return "Java Project node unavailable";
			}

			@Override
			public boolean test() throws Exception {
				return generalNode.isExpanded();
			}
		});
		generalNode.expandNode("Project").select();
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
		bot.textWithLabel("Project name:").setText(TEST_NON_JAVA_PROJECT_NAME);
		bot.button("Finish").click();
		try {
			bot.waitUntil(Conditions.shellIsActive("Open Associated Perspective?"));
			bot.button("Yes").click();
		} catch (final TimeoutException e) {
		}
	}

	@Test
	public void testContextMenuLabelsForJavaProject() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree()
				.getTreeItem(TEST_JAVA_PROJECT_NAME);
		node.setFocus();
		final SWTBotMenu blackDuckMenu = node.select().contextMenu(MenuLabels.BLACK_DUCK);
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.BLACK_DUCK_AUTHORIZATION));
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.BLACK_DUCK_PROXY_SETTINGS));
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.PROJECT_PREFERENCES));
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.SCANNING_PREFERENCES));
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.WARNING_VIEW));
	}

	@Test
	public void testContextMenuLabelsForNonJavaProject() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree()
				.getTreeItem(TEST_NON_JAVA_PROJECT_NAME);
		node.setFocus();
		final SWTBotMenu blackDuckMenu = node.select().contextMenu(MenuLabels.BLACK_DUCK);
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.BLACK_DUCK_AUTHORIZATION));
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.BLACK_DUCK_PROXY_SETTINGS));
		assertNotNull(blackDuckMenu.contextMenu(MenuLabels.SCANNING_PREFERENCES));
	}

}
