package com.blackducksoftware.integration.eclipseplugin.dialogs.listeners;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class NumericInputListener implements VerifyListener {

	private final Text errorText;
	private final String name;

	public NumericInputListener(final Text errorText, final String name) {
		this.errorText = errorText;
		this.name = name;
	}

	@Override
	public void verifyText(final VerifyEvent e) {
		if (!StringUtils.isNumeric(e.text) && e.keyCode != SWT.DEL && e.keyCode != SWT.BS) {
			e.doit = false;
			errorText.setText(name + " textarea only takes numeric input.");
			errorText.setEnabled(true);
			errorText.setVisible(true);
			errorText.getParent().update();
		} else {
			e.doit = true;
		}
	}

}
