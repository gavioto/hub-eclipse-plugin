package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AuthorizationDialog extends Dialog {

	private final AuthorizationValidator validator;

	private Text hubUrl;
	private String hubUrlInputValue;
	private Text username;
	private String usernameInputValue;
	private Text password;
	private String passwordInputValue;

	private Button useDefaultTimeoutButton;
	private boolean useDefaultTimeout;

	private Text timeout;
	private String timeoutInputValue;

	private Button useProxyInfoButton;
	private boolean useProxyInfo;

	private Text proxyPassword;
	private String proxyPasswordInputValue;
	private Text proxyPort;
	private String proxyPortInputValue;
	private Text proxyUsername;
	private String proxyUsernameInputValue;
	private Text proxyHost;
	private String proxyHostInputValue;
	private Text ignoredProxyHosts;
	private String ignoredProxyHostsInputValue;

	private Button testCredentials;
	private Button saveCredentials;
	private final String title;
	private final String message;
	private String errorMessage;
	private Text errorMessageText;

	private final ModifyListener textListener = new ModifyListener() {
		@Override
		public void modifyText(final ModifyEvent e) {
			if (saveCredentials.isEnabled()) {
				saveCredentials.setEnabled(false);
			}
			if (hubUrl.getText() == null || hubUrl.getText().equals("") || username.getText() == null
					|| username.getText().equals("") || password.getText() == null || password.getText().equals("")) {
				if (testCredentials.isEnabled()) {
					testCredentials.setEnabled(false);
				}
			} else if (!testCredentials.isEnabled()) {
				testCredentials.setEnabled(true);
			}
		}
	};

	private final VerifyListener timeoutVerifyListener = new VerifyListener() {
		@Override
		public void verifyText(final VerifyEvent e) {
			if (e.character != '0' && e.character != '1' && e.character != '2' && e.character != '3'
					&& e.character != '4' && e.character != '5' && e.character != '6' && e.character != '7'
					&& e.character != '8' && e.character != '9' && e.keyCode != SWT.DEL && e.keyCode != SWT.BS) {
				e.doit = false;
			} else {
				e.doit = true;
			}

		}
	};

	private final SelectionAdapter useDefaultTimeoutListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (e.getSource() instanceof Button) {
				final Button useDefaultTimeoutButton = (Button) e.getSource();
				useDefaultTimeout = useDefaultTimeoutButton.getSelection();
				timeout.setEnabled(!useDefaultTimeout);
			}
		}
	};
	private final SelectionAdapter useProxyInfoListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (e.getSource() instanceof Button) {
				final Button useProxyInfoButton = (Button) e.getSource();
				useProxyInfo = useProxyInfoButton.getSelection();
				proxyUsername.setEnabled(useProxyInfo);
				proxyPassword.setEnabled(useProxyInfo);
				proxyPort.setEnabled(useProxyInfo);
				proxyHost.setEnabled(useProxyInfo);
				ignoredProxyHosts.setEnabled(useProxyInfo);
			}
		}
	};

	public static final String LOGIN_SUCCESS_MESSAGE = "Successful login!";
	public static final String INCORRECT_CREDENTIALS_MESSAGE = "Incorrect username or password. Please try again";
	public static final String LOGIN_ERROR_MESSAGE = "An error occurred while logging in";
	public static final String CREDENTIAL_MISSING_MESSAGE = "Please enter a hub URL instance, a username, and a password";
	public static final String ERROR_SAVING_CREDENTIALS_MESSAGE = "Error occurred when saving credentials";

	public static final int TEST_CREDENTIALS_ID = 133;
	public static final int SAVE_CREDENTIALS_ID = 134;
	public static final String TEST_CREDENTIALS_LABEL = "Test Hub Connection";
	public static final String SAVE_CREDENTIALS_LABEL = "Save Hub Authorization";

	public AuthorizationDialog(final Shell parentShell, final String dialogTitle, final String dialogMessage,
			final String initialHubUrlValue, final String initialUsernameValue, final String initialPasswordValue,
			final String initialProxyPasswordValue, final String initialProxyPortValue,
			final String initialProxyUsernameValue, final String initialProxyHostValue,
			final String initialIgnoredProxyHostsValue, final String initialTimeoutValue,
			final AuthorizationValidator validator) {
		super(parentShell);
		this.title = dialogTitle;
		this.message = dialogMessage;
		this.validator = validator;
		if (initialHubUrlValue == null) {
			hubUrlInputValue = "";
		} else {
			hubUrlInputValue = initialHubUrlValue;
		}
		if (initialUsernameValue == null) {
			usernameInputValue = "";
		} else {
			usernameInputValue = initialUsernameValue;
		}
		if (initialPasswordValue == null) {
			passwordInputValue = "";
		} else {
			passwordInputValue = initialPasswordValue;
		}
		if (initialProxyPasswordValue == null) {
			proxyPasswordInputValue = "";
		} else {
			proxyPasswordInputValue = initialProxyPasswordValue;
		}
		if (initialProxyPortValue == null) {
			proxyPortInputValue = "";
		} else {
			proxyPortInputValue = initialProxyPortValue;
		}
		if (initialProxyUsernameValue == null) {
			proxyUsernameInputValue = "";
		} else {
			proxyUsernameInputValue = initialProxyUsernameValue;
		}
		if (initialProxyHostValue == null) {
			proxyHostInputValue = "";
		} else {
			proxyHostInputValue = initialProxyHostValue;
		}
		if (initialIgnoredProxyHostsValue == null) {
			ignoredProxyHostsInputValue = "";
		} else {
			ignoredProxyHostsInputValue = initialIgnoredProxyHostsValue;
		}
		if (initialTimeoutValue == null) {
			timeoutInputValue = "";
		} else {
			timeoutInputValue = initialTimeoutValue;
		}
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == TEST_CREDENTIALS_ID) {
			validateInput();
		} else if (buttonId == SAVE_CREDENTIALS_ID) {
			try {
				saveCredentials();
				close();
			} catch (final StorageException e) {
				setErrorMessage(ERROR_SAVING_CREDENTIALS_MESSAGE);
			}
		}
		super.buttonPressed(buttonId);
	}

	private void saveCredentials() throws StorageException {
		final ISecurePreferences defaultNode = SecurePreferencesFactory.getDefault();
		final ISecurePreferences passwordNode = defaultNode.node("Black Duck");
		passwordNode.put("activeHubUrl", hubUrl.getText(), false);
		passwordNode.put("activeUsername", username.getText(), false);
		passwordNode.put("activePassword", password.getText(), true);
		if (useProxyInfo) {
			passwordNode.put("activeProxyUsername", proxyUsername.getText(), false);
			passwordNode.put("activeProxyPassword", proxyPassword.getText(), true);
			passwordNode.put("activeProxyHost", proxyHost.getText(), false);
			passwordNode.put("activeProxyPort", proxyPort.getText(), false);
			passwordNode.put("activeIgnoredProxyHosts", ignoredProxyHosts.getText(), false);
		}
		if (useDefaultTimeout) {
			passwordNode.put("activeTimeout", "120", false);
		} else {
			passwordNode.put("activeTimeout", timeout.getText(), false);
		}
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {

		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		testCredentials = createButton(parent, TEST_CREDENTIALS_ID, TEST_CREDENTIALS_LABEL, true);
		testCredentials.setEnabled(false);
		saveCredentials = createButton(parent, SAVE_CREDENTIALS_ID, SAVE_CREDENTIALS_LABEL, false);
		saveCredentials.setEnabled(false);

		if (hubUrlInputValue != null) {
			hubUrl.setText(hubUrlInputValue);
		} else {
			hubUrl.setText("");
		}
		if (usernameInputValue != null) {
			username.setText(usernameInputValue);
		} else {
			username.setText("");
		}
		if (passwordInputValue != null) {
			password.setText(passwordInputValue);
		} else {
			password.setText("");
		}
		if (timeoutInputValue != null) {
			timeout.setText(timeoutInputValue);
		} else {
			timeout.setText("");
		}
		if (proxyUsernameInputValue != null) {
			proxyUsername.setText(proxyUsernameInputValue);
		} else {
			proxyUsername.setText("");
		}
		if (proxyPasswordInputValue != null) {
			proxyPassword.setText(proxyPasswordInputValue);
		} else {
			proxyPassword.setText("");
		}
		if (proxyPortInputValue != null) {
			proxyPort.setText(proxyPortInputValue);
		} else {
			proxyPort.setText("");
		}
		if (proxyHostInputValue != null) {
			proxyHost.setText(proxyHostInputValue);
		} else {
			proxyHost.setText("");
		}
		if (ignoredProxyHostsInputValue != null) {
			ignoredProxyHosts.setText(ignoredProxyHostsInputValue);
		} else {
			ignoredProxyHosts.setText("");
		}
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		final GridData labelData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		labelData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		final GridData textData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		if (message != null) {
			final Label label = new Label(composite, SWT.WRAP);
			label.setText(message);
			label.setLayoutData(labelData);
			label.setFont(parent.getFont());
		}

		createLabel(parent, composite, labelData, "Hub Instance URL:");
		hubUrl = new Text(composite, SWT.SINGLE | SWT.BORDER);
		hubUrl.setLayoutData(textData);
		hubUrl.addModifyListener(textListener);
		createLabel(parent, composite, labelData, "Username:");
		username = new Text(composite, SWT.SINGLE | SWT.BORDER);
		username.setLayoutData(textData);
		username.addModifyListener(textListener);
		createLabel(parent, composite, labelData, "Password:");
		password = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(textData);
		password.addModifyListener(textListener);

		useDefaultTimeoutButton = new Button(composite, SWT.CHECK);
		useDefaultTimeoutButton.setText("Use default timeout of 120 seconds?");
		useDefaultTimeoutButton.addSelectionListener(useDefaultTimeoutListener);
		useDefaultTimeout = useDefaultTimeoutButton.getSelection();

		createLabel(parent, composite, labelData, "Timeout:");
		timeout = new Text(composite, SWT.SINGLE | SWT.BORDER);
		timeout.setLayoutData(textData);
		timeout.addVerifyListener(timeoutVerifyListener);
		timeout.setEnabled(!useDefaultTimeout);

		useProxyInfoButton = new Button(composite, SWT.CHECK);
		useProxyInfoButton.setText("Use proxy information?");
		useProxyInfoButton.addSelectionListener(useProxyInfoListener);
		useProxyInfo = useProxyInfoButton.getSelection();

		createLabel(parent, composite, labelData, "Proxy Username:");
		proxyUsername = new Text(composite, SWT.SINGLE | SWT.BORDER);
		proxyUsername.setLayoutData(textData);
		proxyUsername.setEnabled(useProxyInfo);
		createLabel(parent, composite, labelData, "Proxy Password:");
		proxyPassword = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		proxyPassword.setLayoutData(textData);
		proxyPassword.setEnabled(useProxyInfo);
		createLabel(parent, composite, labelData, "Proxy Port:");
		proxyPort = new Text(composite, SWT.SINGLE | SWT.BORDER);
		proxyPort.setLayoutData(textData);
		proxyPort.setEnabled(useProxyInfo);
		createLabel(parent, composite, labelData, "Proxy Host:");
		proxyHost = new Text(composite, SWT.SINGLE | SWT.BORDER);
		proxyHost.setLayoutData(textData);
		proxyHost.setEnabled(useProxyInfo);
		createLabel(parent, composite, labelData, "Ignored Proxy Hosts:");
		ignoredProxyHosts = new Text(composite, SWT.SINGLE | SWT.BORDER);
		ignoredProxyHosts.setLayoutData(textData);
		ignoredProxyHosts.setEnabled(useProxyInfo);

		errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		setErrorMessage(errorMessage);

		applyDialogFont(composite);
		return composite;
	}

	private void createLabel(final Composite parent, final Composite composite, final GridData data,
			final String labelText) {
		final Label label = new Label(composite, SWT.WRAP);
		label.setText(labelText);
		label.setLayoutData(data);
		label.setFont(parent.getFont());
	}

	protected void validateInput() {
		String errorMessage = null;
		if (validator != null) {
			errorMessage = validator.isValid(hubUrl.getText(), username.getText(), password.getText(),
					proxyPassword.getText(), proxyPort.getText(), proxyUsername.getText(), proxyHost.getText(),
					ignoredProxyHosts.getText(), timeout.getText(), useDefaultTimeout, useProxyInfo);
		}
		if (errorMessage != null && errorMessage.equals(LOGIN_SUCCESS_MESSAGE)) {
			saveCredentials.setEnabled(true);
		}
		setErrorMessage(errorMessage);
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage);
			final boolean displayError = (errorMessage != null
					&& !StringConverter.removeWhiteSpaces(errorMessage).isEmpty());
			errorMessageText.setEnabled(displayError);
			errorMessageText.setVisible(displayError);
			errorMessageText.getParent().update();
		}
	}

	@Override
	public boolean close() {
		hubUrl.removeModifyListener(textListener);
		username.removeModifyListener(textListener);
		password.removeModifyListener(textListener);
		return super.close();
	}

}
