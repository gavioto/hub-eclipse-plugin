package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.blackducksoftware.integration.eclipseplugin.common.constants.DefaultValues;
import com.blackducksoftware.integration.eclipseplugin.common.constants.DialogTitles;
import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.services.SecurePreferencesService;
import com.blackducksoftware.integration.eclipseplugin.dialogs.listeners.EnableButtonListener;
import com.blackducksoftware.integration.eclipseplugin.dialogs.listeners.EnableTextListener;
import com.blackducksoftware.integration.eclipseplugin.dialogs.listeners.NumericInputListener;

public class AuthorizationDialog extends Dialog {

	private Text hubUrl;
	private Text username;
	private Text password;

	private Button useDefaultTimeoutButton;

	private Text timeout;

	private Button useProxyInfoButton;

	private Button testCredentials;
	private Button saveCredentials;
	private String errorMessage;
	private Text errorMessageText;

	private final SecurePreferencesService prefService;
	private final AuthorizationValidator validator;

	public static final String ERROR_SAVING_CREDENTIALS_MESSAGE = "Error occurred when saving credentials";

	public static final int TEST_CREDENTIALS_ID = 133;
	public static final int SAVE_CREDENTIALS_ID = 134;
	public static final String TEST_CREDENTIALS_LABEL = "Test Hub Connection";
	public static final String SAVE_CREDENTIALS_LABEL = "Save Hub Authorization";

	public static final int HUB_URL_TEXT_INDEX = 0;
	public static final int USERNAME_TEXT_INDEX = 1;
	public static final int PASSWORD_TEXT_INDEX = 2;
	public static final int TIMEOUT_TEXT_INDEX = 3;

	public static final String TITLE = DialogTitles.HUB_AUTHORIZATION;
	public static final String MESSAGE = "Enter your Hub credentials below:";

	public AuthorizationDialog(final Shell parentShell, final SecurePreferencesService prefService,
			final AuthorizationValidator validator) {
		super(parentShell);
		this.prefService = prefService;
		this.validator = validator;
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText(TITLE);
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == TEST_CREDENTIALS_ID) {
			validateInput();
		} else if (buttonId == SAVE_CREDENTIALS_ID) {
			final boolean saveSuccess = saveCredentials();
			if (saveSuccess) {
				close();
			}
		}
		super.buttonPressed(buttonId);
	}

	private boolean saveCredentials() {
		final boolean usernameSaveSuccessful = prefService.saveSecurePreference(SecurePreferenceNames.HUB_USERNAME,
				username.getText(), false);
		final boolean passwordSaveSuccessful = prefService.saveSecurePreference(SecurePreferenceNames.HUB_PASSWORD,
				password.getText(), true);
		final boolean hubUrlSaveSuccessful = prefService.saveSecurePreference(SecurePreferenceNames.HUB_URL, hubUrl.getText(),
				false);
		final String saveErrorMessage = buildSaveErrorMessage(usernameSaveSuccessful, passwordSaveSuccessful,
				hubUrlSaveSuccessful);
		setErrorMessage(saveErrorMessage);
		return usernameSaveSuccessful && passwordSaveSuccessful && hubUrlSaveSuccessful;
	}

	private String buildSaveErrorMessage(final boolean usernameSaveSuccessful, final boolean passwordSaveSuccessful,
			final boolean hubUrlSaveSuccessful) {
		if (usernameSaveSuccessful && passwordSaveSuccessful && hubUrlSaveSuccessful) {
			return "";
		}
		String errorMessage = "Error saving: ";
		boolean firstAddition = true;
		if (!usernameSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "username" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		if (!passwordSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "password" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		if (!hubUrlSaveSuccessful) {
			final String[] stringsToJoin = new String[] { errorMessage, "hub URL" };
			if (firstAddition) {
				errorMessage = StringUtils.join(stringsToJoin, " ");
				firstAddition = false;
			} else {
				errorMessage = StringUtils.join(stringsToJoin, ", ");
			}
		}
		return errorMessage;
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		testCredentials = createButton(parent, TEST_CREDENTIALS_ID, TEST_CREDENTIALS_LABEL, true);
		testCredentials.setEnabled(true);
		saveCredentials = createButton(parent, SAVE_CREDENTIALS_ID, SAVE_CREDENTIALS_LABEL, false);
		saveCredentials.setEnabled(false);

		final String initialHubUrl = prefService.getSecurePreference(SecurePreferenceNames.HUB_URL);
		hubUrl.setText(initialHubUrl);
		hubUrl.addModifyListener(new EnableButtonListener(saveCredentials));
		final String initialUsername = prefService.getSecurePreference(SecurePreferenceNames.HUB_USERNAME);
		username.setText(initialUsername);
		username.addModifyListener(new EnableButtonListener(saveCredentials));
		final String initialPassword = prefService.getSecurePreference(SecurePreferenceNames.HUB_PASSWORD);
		password.setText(initialPassword);
		password.addModifyListener(new EnableButtonListener(saveCredentials));
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		final GridData labelData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		labelData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		final GridData textData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		final Label label = new Label(composite, SWT.WRAP);
		label.setText(MESSAGE);
		label.setLayoutData(labelData);
		label.setFont(parent.getFont());

		createLabel(parent, composite, labelData, "Hub Instance URL:");
		hubUrl = new Text(composite, SWT.SINGLE | SWT.BORDER);
		hubUrl.setLayoutData(textData);
		createLabel(parent, composite, labelData, "Username:");
		username = new Text(composite, SWT.SINGLE | SWT.BORDER);
		username.setLayoutData(textData);
		createLabel(parent, composite, labelData, "Password:");
		password = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(textData);

		useDefaultTimeoutButton = new Button(composite, SWT.CHECK);
		useDefaultTimeoutButton.setText("Use default timeout of 120 seconds?");

		createLabel(parent, composite, labelData, "Timeout:");
		timeout = new Text(composite, SWT.SINGLE | SWT.BORDER);
		timeout.setLayoutData(textData);
		timeout.addVerifyListener(new NumericInputListener());
		timeout.setEnabled(!useDefaultTimeoutButton.getSelection());
		useDefaultTimeoutButton.addSelectionListener(new EnableTextListener(timeout));

		useProxyInfoButton = new Button(composite, SWT.CHECK);
		useProxyInfoButton.setText("Use proxy information?");

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
			String username;
			String password;
			String hubUrl;
			if (this.username != null) {
				username = this.username.getText();
			} else {
				username = "";
			}
			if (this.password != null) {
				password = this.password.getText();
			} else {
				password = "";
			}
			if (this.hubUrl != null) {
				hubUrl = this.hubUrl.getText();
			} else {
				hubUrl = "";
			}
			String proxyUsername;
			String proxyPassword;
			String proxyPort;
			String proxyHost;
			String ignoredProxyHosts;
			final boolean useProxyInfo = useProxyInfoButton.getSelection();
			if (useProxyInfo) {
				proxyUsername = prefService.getSecurePreference(SecurePreferenceNames.PROXY_USERNAME);
				proxyPassword = prefService.getSecurePreference(SecurePreferenceNames.PROXY_PASSWORD);
				proxyPort = prefService.getSecurePreference(SecurePreferenceNames.PROXY_PORT);
				proxyHost = prefService.getSecurePreference(SecurePreferenceNames.PROXY_HOST);
				ignoredProxyHosts = prefService.getSecurePreference(SecurePreferenceNames.IGNORED_PROXY_HOSTS);
			} else {
				proxyUsername = DefaultValues.PROXY_USERNAME;
				proxyPassword = DefaultValues.PROXY_PASSWORD;
				proxyPort = DefaultValues.PROXY_PORT;
				proxyHost = DefaultValues.PROXY_HOST;
				ignoredProxyHosts = DefaultValues.IGNORED_PROXY_HOSTS;
			}
			String timeout;
			final boolean useDefaultTimeout = useDefaultTimeoutButton.getSelection();
			if (useDefaultTimeout) {
				timeout = DefaultValues.TIMEOUT;
			} else {
				if (this.timeout != null) {
					timeout = this.timeout.getText();
				} else {
					timeout = "";
				}
			}
			errorMessage = validator.validateCredentials(username, password, hubUrl, proxyUsername, proxyPassword,
					proxyPort, proxyHost, ignoredProxyHosts, timeout);

		}
		setSaveCredentialsEnabled(errorMessage);
		setErrorMessage(errorMessage);
	}

	protected void setSaveCredentialsEnabled(final String errorMessage) {
		if (errorMessage != null && errorMessage.equals(AuthorizationValidator.LOGIN_SUCCESS_MESSAGE)) {
			saveCredentials.setEnabled(true);
		} else {
			if (saveCredentials.isEnabled()) {
				saveCredentials.setEnabled(false);
			}
		}
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
		return super.close();
	}

}
