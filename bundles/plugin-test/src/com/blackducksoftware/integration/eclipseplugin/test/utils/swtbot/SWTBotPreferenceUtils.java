package com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

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

}
