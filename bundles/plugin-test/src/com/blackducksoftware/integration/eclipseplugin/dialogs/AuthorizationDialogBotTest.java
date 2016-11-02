package com.blackducksoftware.integration.eclipseplugin.dialogs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.blackducksoftware.integration.eclipseplugin.common.constants.DialogTitles;
import com.blackducksoftware.integration.eclipseplugin.common.constants.MenuLabels;
import com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot.SWTBotProjectUtils;

@RunWith(SWTBotJunit4ClassRunner.class)
public class AuthorizationDialogBotTest {

	private static SWTWorkbenchBot bot;
	private static SWTBotProjectUtils botProjectUtils;
	private final static String TEST_PROJECT_NAME = "authorization-dialog-test-project";

	@BeforeClass
	public static void setupWorkspaceBot() {
		bot = new SWTWorkbenchBot();
		botProjectUtils = new SWTBotProjectUtils(bot);
		try {
			bot.viewByTitle("Welcome").close();
		} catch (final RuntimeException e) {
		}
		botProjectUtils.createJavaProject(TEST_PROJECT_NAME);
	}

	@Test
	public void timeoutTextareaOnlyAllowsNumberInput() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		node.select().contextMenu(MenuLabels.BLACK_DUCK).contextMenu(MenuLabels.BLACK_DUCK_AUTHORIZATION).click();
		bot.waitUntil(Conditions.shellIsActive(DialogTitles.HUB_AUTHORIZATION));
		final SWTBotText timeoutText = bot.text(AuthorizationDialog.TIMEOUT_TEXT_INDEX);
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

		bot.shell(DialogTitles.HUB_AUTHORIZATION).close();

	}

	@Test
	public void saveCredentialsNotEnabled() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		node.select().contextMenu(MenuLabels.BLACK_DUCK).contextMenu(MenuLabels.BLACK_DUCK_AUTHORIZATION).click();
		bot.waitUntil(Conditions.shellIsActive(DialogTitles.HUB_AUTHORIZATION));
		final SWTBotButton saveCredsButton = bot.button("Save Hub Authorization");
		assertFalse(saveCredsButton.isEnabled());
		bot.shell(DialogTitles.HUB_AUTHORIZATION).close();
	}

	@Test
	public void disableAndEnableTimeoutWorks() {
		final SWTBotTreeItem node = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(TEST_PROJECT_NAME);
		node.setFocus();
		node.select().contextMenu(MenuLabels.BLACK_DUCK).contextMenu(MenuLabels.BLACK_DUCK_AUTHORIZATION).click();
		bot.waitUntil(Conditions.shellIsActive(DialogTitles.HUB_AUTHORIZATION));
		final SWTBotCheckBox disableTimeoutButton = bot.checkBox("Use default timeout of 120 seconds?");
		final SWTBotText timeoutText = bot.text(AuthorizationDialog.TIMEOUT_TEXT_INDEX);
		assertFalse(disableTimeoutButton.isChecked());
		assertTrue(timeoutText.isEnabled());
		disableTimeoutButton.click();
		assertTrue(disableTimeoutButton.isChecked());
		assertFalse(timeoutText.isEnabled());
	}

	@AfterClass
	public static void teardownWorkspaceBot() {
		botProjectUtils.deleteProjectFromDisk(TEST_PROJECT_NAME);
		bot.resetWorkbench();
	}

}
