package com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot;

import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

public class TreeItemIsExpandedCondition extends DefaultCondition {

	private final SWTBotTreeItem item;

	public TreeItemIsExpandedCondition(final SWTBotTreeItem item) {
		this.item = item;
	}

	@Override
	public boolean test() throws Exception {
		return item.isExpanded();
	}

	@Override
	public String getFailureMessage() {
		return "could not expand " + item.getText() + "node";
	}

}
