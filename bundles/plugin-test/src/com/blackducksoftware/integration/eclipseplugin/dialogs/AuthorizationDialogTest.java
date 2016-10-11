package com.blackducksoftware.integration.eclipseplugin.dialogs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class AuthorizationDialogTest {

	private static SWTWorkbenchBot bot;
	private final static String TEST_PROJECT_NAME = "authorization-dialog-test-project";

	@BeforeClass
	public static void setupWorkspaceBot() {
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

	@Test
	public void timeoutTextareaOnlyAllowsNumberInput() {
		SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		node.select().contextMenu("Black Duck").contextMenu("Black Duck Authorization...").click();
		bot.waitUntil(Conditions.shellIsActive("Hub Authorization"));
		SWTBotText timeoutText = bot.text(AuthorizationDialog.TIMEOUT_TEXT_INDEX);
		timeoutText.setText("abcd");
		assertTrue(timeoutText.getText().equals(""));
		timeoutText.setText("j4ie;o2");
		assertTrue(timeoutText.getText().equals(""));
		
		timeoutText.setText("74724749");
		assertTrue(timeoutText.getText().equals("74724749"));
		
		timeoutText.setText("0");
		assertTrue(timeoutText.getText().equals("0"));
		timeoutText.setText("1");
		assertTrue(timeoutText.getText().equals("1"));
		timeoutText.setText("2");
		assertTrue(timeoutText.getText().equals("2"));
		timeoutText.setText("3");
		assertTrue(timeoutText.getText().equals("3"));
		timeoutText.setText("4");
		assertTrue(timeoutText.getText().equals("4"));
		timeoutText.setText("5");
		assertTrue(timeoutText.getText().equals("5"));
		timeoutText.setText("6");
		assertTrue(timeoutText.getText().equals("6"));
		timeoutText.setText("7");
		assertTrue(timeoutText.getText().equals("7"));
		timeoutText.setText("8");
		assertTrue(timeoutText.getText().equals("8"));
		timeoutText.setText("9");
		assertTrue(timeoutText.getText().equals("9"));
		
		timeoutText.setText("23");
		timeoutText.typeText("afje");
		assertTrue(timeoutText.getText().equals("23"));
		
		bot.shell("Hub Authorization").close();
		
	}
	
	@Test
	public void saveCredentialsNotEnabled() {
		SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		node.select().contextMenu("Black Duck").contextMenu("Black Duck Authorization...").click();
		bot.waitUntil(Conditions.shellIsActive("Hub Authorization"));
		SWTBotButton saveCredsButton = bot.button("Save Hub Authorization");
		assertFalse(saveCredsButton.isEnabled());
		bot.shell("Hub Authorization").close();
	}
	
	@Test
	public void disableAndEnableTimeoutWorks() {
		SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		node.select().contextMenu("Black Duck").contextMenu("Black Duck Authorization...").click();
		bot.waitUntil(Conditions.shellIsActive("Hub Authorization"));
		SWTBotCheckBox disableTimeoutButton = bot.checkBox("Use default timeout of 120 seconds?");
		SWTBotText timeoutText = bot.text(AuthorizationDialog.TIMEOUT_TEXT_INDEX);
		assertFalse(disableTimeoutButton.isChecked());
		assertTrue(timeoutText.isEnabled());
		disableTimeoutButton.click();
		assertTrue(disableTimeoutButton.isChecked());
		assertFalse(timeoutText.isEnabled());
	}

	@AfterClass
	public static void teardownWorkspaceBot() {
		bot.resetWorkbench();
	}

}
