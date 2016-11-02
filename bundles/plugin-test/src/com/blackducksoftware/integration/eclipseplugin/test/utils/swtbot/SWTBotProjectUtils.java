package com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

public class SWTBotProjectUtils {

	private final SWTWorkbenchBot bot;

	public SWTBotProjectUtils(final SWTWorkbenchBot bot) {
		this.bot = bot;
	}

	public void createJavaProject(final String projectName) {
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
		bot.textWithLabel("Project name:").setText(projectName);
		bot.button("Finish").click();
		try {
			bot.waitUntil(Conditions.shellIsActive("Open Associated Perspective?"));
			bot.button("Yes").click();
		} catch (final TimeoutException e) {
		}
	}

	public void createNonJavaProject(final String projectName) {
		final SWTBotMenu fileMenu = bot.menu("File");
		final SWTBotMenu projectMenu = fileMenu.menu("New");
		final SWTBotMenu newMenu = projectMenu.menu("Project...");
		newMenu.click();
		bot.waitUntil(Conditions.shellIsActive("New Project"));
		final SWTBotTree optionTree = bot.tree();
		final SWTBotTreeItem generalNode = optionTree.expandNode("General");
		bot.waitUntil(new DefaultCondition() {
			@Override
			public String getFailureMessage() {
				return "General node unavailable";
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
		bot.textWithLabel("Project name:").setText(projectName);
		bot.button("Finish").click();
		try {
			bot.waitUntil(Conditions.shellIsActive("Open Associated Perspective?"));
			bot.button("Yes").click();
		} catch (final TimeoutException e) {
		}
	}

	public void createMavenProject(final String groupId, final String artifactId) {
		final SWTBotMenu fileMenu = bot.menu("File");
		final SWTBotMenu projectMenu = fileMenu.menu("New");
		final SWTBotMenu newMenu = projectMenu.menu("Project...");
		newMenu.click();
		bot.waitUntil(Conditions.shellIsActive("New Project"));
		final SWTBotTree optionTree = bot.tree();
		final SWTBotTreeItem mavenNode = optionTree.expandNode("Maven");
		bot.waitUntil(new DefaultCondition() {
			@Override
			public String getFailureMessage() {
				return "Maven node unavailable";
			}

			@Override
			public boolean test() throws Exception {
				return mavenNode.isExpanded();
			}
		});
		mavenNode.expandNode("Maven Project").select();
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
		bot.checkBox("Create a simple project (skip archetype selection)").select();
		bot.button("Next >").click();
		bot.comboBox(0).setText(groupId);
		bot.comboBox(1).setText(artifactId);
		bot.waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				return bot.button("Finish").isEnabled();
			}

			@Override
			public String getFailureMessage() {
				return "unable to select Finish button";
			}

		});
		bot.button("Finish").click();
		try {
			bot.waitUntil(Conditions.shellIsActive("Open Associated Perspective?"));
			bot.button("Yes").click();
		} catch (final TimeoutException e) {
		}
		bot.sleep(2000);
	}

	public void updateMavenProject(final String projectName) {
		final SWTBotTreeItem mavenProjectNode = bot.viewByTitle("Package Explorer").bot().tree()
				.getTreeItem(projectName);
		final SWTBotMenu mavenMenu = mavenProjectNode.contextMenu().menu("Maven");
		mavenMenu.menu("Update Project...").click();
		bot.waitUntil(Conditions.shellIsActive("Update Maven Project"));
		bot.waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				return bot.button("OK").isEnabled();
			}

			@Override
			public String getFailureMessage() {
				return "OK button not enabled";
			}

		});
		bot.button("OK").click();
	}

	public void addMavenDependency(final String projectName, final String groupId, final String artifactId,
			final String version) {
		final SWTBotTreeItem mavenProjectNode = bot.viewByTitle("Package Explorer").bot().tree()
				.getTreeItem(projectName);
		final SWTBotMenu mavenMenu = mavenProjectNode.contextMenu().menu("Maven");
		mavenMenu.menu("Add Dependency").click();
		bot.waitUntil(Conditions.shellIsActive("Add Dependency"));
		bot.text(0).setText(groupId);
		bot.text(1).setText(artifactId);
		bot.text(2).setText(version);
		bot.waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				return bot.button("OK").isEnabled();
			}

			@Override
			public String getFailureMessage() {
				return "OK button not enabled";
			}

		});
		bot.button("OK").click();
	}

	public void deleteProjectFromDisk(final String projectName) {
		final SWTBotTreeItem nonJavaProjectNode = bot.viewByTitle("Package Explorer").bot().tree()
				.getTreeItem(projectName);
		nonJavaProjectNode.contextMenu().menu("Delete").click();
		bot.waitUntil(Conditions.shellIsActive("Delete Resources"));
		bot.checkBox().select();
		bot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Delete Resources")));
		} catch (final WidgetNotFoundException e) {
		}
	}

	public void deleteProjectFromWorkspace(final String projectName, final SWTWorkbenchBot bot) {
		final SWTBotTreeItem nonJavaProjectNode = bot.viewByTitle("Package Explorer").bot().tree()
				.getTreeItem(projectName);
		nonJavaProjectNode.contextMenu().menu("Delete").click();
		bot.waitUntil(Conditions.shellIsActive("Delete Resources"));
		bot.button("OK").click();
		try {
			bot.waitUntil(Conditions.shellCloses(bot.shell("Delete Resources")));
		} catch (final WidgetNotFoundException e) {
		}
	}

}
