package com.blackducksoftware.integration.eclipseplugin.views;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
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

public class WarningViewTest {
	
	public static SWTWorkbenchBot bot;
	
	private static final String TEST_PROJECT_NAME = "warning-view-test-project";
	
	@BeforeClass
	public static void setUpWorkspaceBot() {
		bot = new SWTWorkbenchBot();
		try {
			bot.viewByTitle("Welcome").close();
		} catch (RuntimeException e) {}
		SWTBotMenu fileMenu = bot.menu("File");
		SWTBotMenu projectMenu = fileMenu.menu("New");
		SWTBotMenu newMenu = projectMenu.menu("Project...");
		newMenu.click();
		bot.waitUntil(Conditions.shellIsActive("New Project"));
		SWTBotTree optionTree = bot.tree();
		SWTBotTreeItem javaNode = optionTree.expandNode("Java");
		bot.waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "Java Project node unavailable";
			}
			
			public boolean test() throws Exception {
				return javaNode.isExpanded();
			}
		});
		javaNode.expandNode("Java Project").select();
		bot.waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "unable to select Next button";
			}
			
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
		} catch (TimeoutException e) {}
	}
	
	@Ignore
	public void testThatWarningViewOpensFromContextMenu() {
		SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		SWTBotMenu warningViewMenu = node.select().contextMenu("Black Duck").contextMenu("Open Warning View");
		warningViewMenu.click();
		bot.waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "could not find details button";
			}
			
			public boolean test() {
				return bot.button("Details >>").isEnabled();
			}
		});
		bot.button("Details >>").click();
		assertNotNull(bot.viewByTitle("Warning View"));
		assertTrue(bot.viewById("com.blackducksoftware.integration.eclipseplugin.views.WarningView").isActive());
		bot.viewByTitle("Warning View").close();
	}
	
	@Ignore
	public void testThatWarningViewOpensFromWindowMenu() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.waitUntil(Conditions.shellIsActive("Show View"));
		bot.tree().expandNode("Black Duck").expandNode("Warning View").select();
		bot.waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "ok button could not be enabled";
			}
			public boolean test() {
				return bot.button("OK").isEnabled();
			}
		});
		bot.button("OK").click();
		bot.button("<< Details").click();
		bot.sleep(5000);
		assertNotNull(bot.viewByTitle("Warning View"));
		assertTrue(bot.viewByTitle("Warning View").isActive());
		bot.viewByTitle("Warning View").close();
	}
	
	@AfterClass
	public static void tearDownWorkspaceBot() {
		bot.resetWorkbench();
	}
	
}
