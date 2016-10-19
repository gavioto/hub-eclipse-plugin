package com.blackducksoftware.integration.eclipseplugin.dialogs.listeners;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class NumericInputListener implements VerifyListener {

	@Override
	public void verifyText(final VerifyEvent e) {
		if (!StringUtils.isNumeric(e.text) && e.keyCode != SWT.DEL && e.keyCode != SWT.BS) {
			e.doit = false;
		} else {
			e.doit = true;
		}
	}

}
