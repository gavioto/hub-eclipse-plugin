package com.blackducksoftware.integration.eclipseplugin.dialogs.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;

public class EnableButtonListener implements ModifyListener {

	private final Button button;

	public EnableButtonListener(final Button button) {
		this.button = button;
	}

	@Override
	public void modifyText(final ModifyEvent e) {
		if (button != null && button.isEnabled()) {
			button.setEnabled(false);
		}
	}

}
