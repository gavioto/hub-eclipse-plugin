package com.blackducksoftware.integration.eclipseplugin.test.utils.swtbot;

import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;

public class ButtonIsEnabledCondition extends DefaultCondition {

	private final SWTBotButton button;

	public ButtonIsEnabledCondition(final SWTBotButton button) {
		this.button = button;
	}

	@Override
	public boolean test() throws Exception {
		return button.isEnabled();
	}

	@Override
	public String getFailureMessage() {
		return "Could not enable " + button.getText() + " button";
	}

}
