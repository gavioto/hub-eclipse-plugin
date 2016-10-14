package com.blackducksoftware.integration.eclipseplugin.dialogs;

import org.apache.commons.lang3.StringUtils;
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

import com.blackducksoftware.integration.eclipseplugin.common.constants.DefaultValues;
import com.blackducksoftware.integration.eclipseplugin.common.constants.DialogTitles;
import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.utils.SecurePreferencesService;

public class AuthorizationDialog extends Dialog {

	private Text hubUrl;
	private Text username;
	private Text password;

	private Button useDefaultTimeoutButton;
	private boolean useDefaultTimeout;

	private Text timeout;

	private Button useProxyInfoButton;
	private boolean useProxyInfo;

	private Button testCredentials;
	private Button saveCredentials;
	private String errorMessage;
	private Text errorMessageText;

	private final SecurePreferencesService prefService;
	private final AuthorizationValidator validator;

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
			if (!StringUtils.isNumeric(e.text) && e.keyCode != SWT.DEL && e.keyCode != SWT.BS) {
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
			}
		}
	};

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
		final boolean usernameSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.HUB_USERNAME,
				username.getText(), false);
		final boolean passwordSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.HUB_PASSWORD,
				password.getText(), true);
		final boolean hubUrlSaveSuccessful = prefService.saveSecurePreference(PreferenceNames.HUB_URL, hubUrl.getText(),
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
		testCredentials.setEnabled(false);
		saveCredentials = createButton(parent, SAVE_CREDENTIALS_ID, SAVE_CREDENTIALS_LABEL, false);
		saveCredentials.setEnabled(false);

		final String initialHubUrl = prefService.getSecurePreference(PreferenceNames.HUB_URL);
		hubUrl.setText(initialHubUrl);
		final String initialUsername = prefService.getSecurePreference(PreferenceNames.HUB_USERNAME);
		username.setText(initialUsername);
		final String initialPassword = prefService.getSecurePreference(PreferenceNames.HUB_PASSWORD);
		password.setText(initialPassword);
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
			if (useProxyInfo) {
				proxyUsername = prefService.getSecurePreference(PreferenceNames.PROXY_USERNAME);
				proxyPassword = prefService.getSecurePreference(PreferenceNames.PROXY_PASSWORD);
				proxyPort = prefService.getSecurePreference(PreferenceNames.PROXY_PORT);
				proxyHost = prefService.getSecurePreference(PreferenceNames.PROXY_HOST);
				ignoredProxyHosts = prefService.getSecurePreference(PreferenceNames.IGNORED_PROXY_HOSTS);
			} else {
				proxyUsername = DefaultValues.PROXY_USERNAME;
				proxyPassword = DefaultValues.PROXY_PASSWORD;
				proxyPort = DefaultValues.PROXY_PORT;
				proxyHost = DefaultValues.PROXY_HOST;
				ignoredProxyHosts = DefaultValues.IGNORED_PROXY_HOSTS;
			}
			String timeout;
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
		hubUrl.removeModifyListener(textListener);
		username.removeModifyListener(textListener);
		password.removeModifyListener(textListener);
		return super.close();
	}

}
