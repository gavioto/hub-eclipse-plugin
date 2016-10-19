package com.blackducksoftware.integration.eclipseplugin.dialogs.listeners;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

public class EnableTextListener extends SelectionAdapter {

	private final Text textArea;

	public EnableTextListener(final Text textArea) {
		this.textArea = textArea;
	}

	@Override
	public void widgetSelected(final SelectionEvent e) {
		if (e.getSource() instanceof Button) {
			final Button useDefaultTimeoutButton = (Button) e.getSource();
			textArea.setEnabled(!useDefaultTimeoutButton.getSelection());
		}
	}
}
