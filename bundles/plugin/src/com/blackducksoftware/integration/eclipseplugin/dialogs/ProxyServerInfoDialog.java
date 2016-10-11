package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProxyServerInfoDialog extends Dialog {

	private final String title;
	private final String message;

	private final String proxyPasswordInputValue;
	private final String proxyPortInputValue;
	private final String proxyUsernameInputValue;
	private final String proxyHostInputValue;
	private final String ignoredProxyHostsInputValue;

	private Text proxyPassword;
	private Text proxyPort;
	private Text proxyUsername;
	private Text proxyHost;
	private Text ignoredProxyHosts;

	private Text saveErrorMessage;

	public static final String ERROR_SAVING_PROXY_INFO_MESSAGE = "Error encountered saving proxy server information";

	public ProxyServerInfoDialog(final Shell parentShell, final String dialogTitle, final String dialogMessage,
			final String initialProxyPasswordValue, final String initialProxyPortValue,
			final String initialProxyUsernameValue, final String initialProxyHostValue,
			final String initialIgnoredProxyHostsValue) {
		super(parentShell);
		title = dialogTitle;
		message = dialogMessage;
		proxyPasswordInputValue = initialProxyPasswordValue;
		proxyPortInputValue = initialProxyPortValue;
		proxyUsernameInputValue = initialProxyUsernameValue;
		ignoredProxyHostsInputValue = initialIgnoredProxyHostsValue;
		proxyHostInputValue = initialProxyHostValue;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		final Composite composite = (Composite) super.createDialogArea(parent);
		final GridData labelData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		labelData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		final GridData textData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		if (message != null) {
			final Label messageLabel = new Label(composite, SWT.WRAP);
			messageLabel.setText(message);
			messageLabel.setLayoutData(labelData);
			messageLabel.setFont(parent.getFont());
		}

		createLabel(parent, composite, labelData, "Proxy Username:");
		proxyUsername = new Text(composite, SWT.SINGLE | SWT.BORDER);
		proxyUsername.setLayoutData(textData);
		createLabel(parent, composite, labelData, "Proxy Password:");
		proxyPassword = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		proxyPassword.setLayoutData(textData);
		createLabel(parent, composite, labelData, "Proxy Port:");
		proxyPort = new Text(composite, SWT.SINGLE | SWT.BORDER);
		proxyPort.setLayoutData(textData);
		createLabel(parent, composite, labelData, "Proxy Host:");
		proxyHost = new Text(composite, SWT.SINGLE | SWT.BORDER);
		proxyHost.setLayoutData(textData);
		createLabel(parent, composite, labelData, "Ignored Proxy Hosts:");
		ignoredProxyHosts = new Text(composite, SWT.SINGLE | SWT.BORDER);
		ignoredProxyHosts.setLayoutData(textData);
		saveErrorMessage = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		saveErrorMessage.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		saveErrorMessage.setBackground(saveErrorMessage.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		return composite;
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	private void createLabel(final Composite parent, final Composite composite, final GridData data,
			final String labelText) {
		final Label label = new Label(composite, SWT.WRAP);
		label.setText(labelText);
		label.setLayoutData(data);
		label.setFont(parent.getFont());
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		if (proxyPassword != null && proxyPasswordInputValue != null) {
			proxyPassword.setText(proxyPasswordInputValue);
		}
		if (proxyPort != null && proxyPortInputValue != null) {
			proxyPort.setText(proxyPortInputValue);
		}
		if (proxyUsername != null && proxyUsernameInputValue != null) {
			proxyUsername.setText(proxyUsernameInputValue);
		}
		if (proxyHost != null && proxyHostInputValue != null) {
			proxyHost.setText(proxyHostInputValue);
		}
		if (ignoredProxyHosts != null && ignoredProxyHostsInputValue != null) {
			ignoredProxyHosts.setText(ignoredProxyHostsInputValue);
		}
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			try {
				saveProxyInfo();
				saveErrorMessage.setText(" \n ");
				super.buttonPressed(buttonId);
			} catch (final StorageException e) {
				if (saveErrorMessage != null) {
					saveErrorMessage.setText(ERROR_SAVING_PROXY_INFO_MESSAGE);
				}
			}
		} else {
			saveErrorMessage.setText(" \n ");
			super.buttonPressed(buttonId);
		}
	}

	private void saveProxyInfo() throws StorageException {
		final ISecurePreferences defaultNode = SecurePreferencesFactory.getDefault();
		final ISecurePreferences blackDuckNode = defaultNode.node("Black Duck");
		if (proxyPassword != null) {
			blackDuckNode.put("activeProxyPassword", proxyPassword.getText(), true);
		}
		if (proxyPort != null) {
			blackDuckNode.put("activeProxyPort", proxyPort.getText(), false);
		}
		if (proxyUsername != null) {
			blackDuckNode.put("activeProxyUsername", proxyUsername.getText(), false);
		}
		if (ignoredProxyHosts != null) {
			blackDuckNode.put("activeIgnoredProxyHosts", ignoredProxyHosts.getText(), false);
		}
		if (proxyHost != null) {
			blackDuckNode.put("activeProxyHost", proxyHost.getText(), false);
		}
	}
}
