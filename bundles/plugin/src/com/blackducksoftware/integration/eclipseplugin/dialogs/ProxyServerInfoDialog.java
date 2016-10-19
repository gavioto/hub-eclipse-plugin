package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.services.SecurePreferencesService;
import com.blackducksoftware.integration.eclipseplugin.dialogs.listeners.NumericInputListener;

public class ProxyServerInfoDialog extends Dialog {

	private final String title;
	private final String message;

	private Text proxyPassword;
	private Text proxyPort;
	private Text proxyUsername;
	private Text proxyHost;
	private Text ignoredProxyHosts;

	private Text saveErrorMessage;

	private final SecurePreferencesService prefService;

	public static final String ERROR_SAVING_PROXY_INFO_MESSAGE = "Error encountered saving proxy server information";

	public ProxyServerInfoDialog(final Shell parentShell, final SecurePreferencesService prefService,
			final String dialogTitle, final String dialogMessage) {
		super(parentShell);
		title = dialogTitle;
		message = dialogMessage;
		this.prefService = prefService;
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
		proxyPort.addVerifyListener(new NumericInputListener());
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
		createButton(parent, IDialogConstants.OK_ID, "Save Proxy Credentials", true);
		final String initialProxyUsername = prefService.getSecurePreference(PreferenceNames.PROXY_USERNAME);
		proxyUsername.setText(initialProxyUsername);
		final String initialProxyPassword = prefService.getSecurePreference(PreferenceNames.PROXY_PASSWORD);
		proxyPassword.setText(initialProxyPassword);
		final String initialProxyPort = prefService.getSecurePreference(PreferenceNames.PROXY_PORT);
		proxyPort.setText(initialProxyPort);
		final String initialProxyHost = prefService.getSecurePreference(PreferenceNames.PROXY_HOST);
		proxyHost.setText(initialProxyHost);
		final String initialIgnoredProxyHosts = prefService.getSecurePreference(PreferenceNames.IGNORED_PROXY_HOSTS);
		ignoredProxyHosts.setText(initialIgnoredProxyHosts);
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			final boolean saveSuccess = saveProxyInfo();
			if (saveSuccess) {
				super.buttonPressed(buttonId);
			}
		} else {
			saveErrorMessage.setText(" \n ");
			super.buttonPressed(buttonId);
		}
	}

	private String buildSaveErrorMessage(final boolean proxyPasswordSaveSuccessful,
			final boolean proxyPortSaveSuccessful, final boolean proxyUsernameSaveSuccessful,
			final boolean ignoredProxyHostsSaveSuccessful, final boolean proxyHostSaveSuccessful) {
		if (proxyPasswordSaveSuccessful && proxyPortSaveSuccessful && proxyUsernameSaveSuccessful
				&& ignoredProxyHostsSaveSuccessful && proxyHostSaveSuccessful) {
			return "";
		}
		String errorMessage = "Error saving: ";
		boolean firstAddition = true;
		if (!proxyPasswordSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "proxy password" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		if (!proxyPortSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "proxy port" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		if (!proxyUsernameSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "proxy username" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		if (!ignoredProxyHostsSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "ignored proxy hosts" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		if (!proxyHostSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "proxy host" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		return errorMessage;
	}

	private boolean saveProxyInfo() {
		final boolean proxyPasswordSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.PROXY_PASSWORD,
				proxyPassword.getText(), true);
		final boolean proxyPortSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.PROXY_PORT,
				proxyPort.getText(), false);
		final boolean proxyUsernameSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.PROXY_USERNAME,
				proxyUsername.getText(), false);
		final boolean ignoredProxyHostsSaveSuccessful = prefService
				.saveSecurePreference(PreferenceNames.IGNORED_PROXY_HOSTS, ignoredProxyHosts.getText(), false);
		final boolean proxyHostSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.PROXY_HOST,
				proxyHost.getText(), false);
		final String message = buildSaveErrorMessage(proxyPasswordSaveSuccessful, proxyPortSaveSuccessful,
				proxyUsernameSaveSuccessful, ignoredProxyHostsSaveSuccessful, proxyHostSaveSuccessful);
		saveErrorMessage.setText(message);
		return proxyPasswordSaveSuccessful && proxyPortSaveSuccessful && proxyUsernameSaveSuccessful
				&& ignoredProxyHostsSaveSuccessful && proxyHostSaveSuccessful;
	}
}
