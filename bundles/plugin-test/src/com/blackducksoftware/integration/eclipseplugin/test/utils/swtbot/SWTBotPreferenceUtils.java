package com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferencePageNames;
import com.blackducksoftware.integration.eclipseplugin.preferences.PreferenceDefaults;

public class SWTBotPreferenceUtils {

	private final SWTWorkbenchBot bot;

	public SWTBotPreferenceUtils(final SWTWorkbenchBot bot) {
		this.bot = bot;
	}

	public void openBlackDuckPreferencesFromContextMenu(final String projectName) {
		final SWTBotTreeItem project = bot.viewByTitle("Package Explorer").bot().tree().getTreeItem(projectName);
		project.contextMenu().menu("Black Duck").menu("Scanning Preferences...").click();
		bot.waitUntil(Conditions.shellIsActive("Preferences (Filtered)"));
	}

	public void openBlackDuckPreferencesFromEclipseMenu() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					final Menu appMenu = workbench.getDisplay().getSystemMenu();
					for (final MenuItem item : appMenu.getItems()) {
						if (item.getText().startsWith("Preferences")) {
							final Event event = new Event();
							event.time = (int) System.currentTimeMillis();
							event.widget = item;
							event.display = workbench.getDisplay();
							item.setSelection(true);
							item.notifyListeners(SWT.Selection, event);
							break;
						}
					}
				}
			}
		});
		bot.waitUntil(Conditions.shellIsActive("Preferences"));
	}

	public void setPrefsToActivateScanByDefault() {
		getDefaultSettingsPage();
		final SWTBot pageBot = bot.activeShell().bot();
		pageBot.radio(PreferenceDefaults.ACTIVATE_BY_DEFAULT).click();
		pageBot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Preferences")));
		} catch (final WidgetNotFoundException e) {

		}
	}

	public void setPrefsToNotActivateScanByDefault() {
		getDefaultSettingsPage();
		final SWTBot pageBot = bot.activeShell().bot();
		pageBot.radio(PreferenceDefaults.DO_NOT_ACTIVATE_BY_DEFAULT).click();
		pageBot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Preferences")));
		} catch (final WidgetNotFoundException e) {

		}
	}

	public void activateProject(final String projectName) {
		getActiveJavaProjectsPage();
		final SWTBot pageBot = bot.activeShell().bot();
		pageBot.checkBox(projectName).select();
		pageBot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Preferences")));
		} catch (final WidgetNotFoundException e) {

		}
	}

	public void deactivateProject(final String projectName) {
		getActiveJavaProjectsPage();
		final SWTBot pageBot = bot.activeShell().bot();
		pageBot.checkBox(projectName).deselect();
		pageBot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Preferences")));
		} catch (final WidgetNotFoundException e) {

		}
	}

	public void restoreAllBlackDuckDefaults() {
		getDefaultSettingsPage();
		final SWTBot pageBot = bot.activeShell().bot();
		pageBot.button("Restore Defaults").click();
		bot.sleep(5000);
		pageBot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Preferences")));
		} catch (final WidgetNotFoundException e) {

		}
	}

	public void getActiveJavaProjectsPage() {
		openBlackDuckPreferencesFromEclipseMenu();
		final SWTBot prefBot = bot.activeShell().bot();
		final SWTBotTreeItem blackDuck = prefBot.tree().expandNode(PreferencePageNames.BLACK_DUCK);
		bot.waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				return blackDuck.isExpanded();
			}

			@Override
			public String getFailureMessage() {
				return "could not expand Black Duck preference node";
			}

		});
		blackDuck.getNode(PreferencePageNames.ACTIVE_JAVA_PROJECTS).click();
	}

	public void getDefaultSettingsPage() {
		openBlackDuckPreferencesFromEclipseMenu();
		final SWTBot pageBot = bot.activeShell().bot();
		final SWTBotTreeItem blackDuck = pageBot.tree().expandNode(PreferencePageNames.BLACK_DUCK);
		bot.waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				return blackDuck.isExpanded();
			}

			@Override
			public String getFailureMessage() {
				return "Could not expand Black Duck preference node";
			}

		});
		blackDuck.getNode(PreferencePageNames.BLACK_DUCK_DEFAULTS).click();
	}

}
