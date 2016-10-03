package com.blackducksoftware.integration.eclipseplugin.dialogs;

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

public class AuthorizationDialog extends Dialog {

	private final AuthorizationValidator validator;
	private Text username;
	private String usernameInputValue;
	// hide password text??
	private Text password;
	private final String passwordInputValue;
	// enable testCredentials only if both inputs are nonempty
	private Button testCredentials;
	// enable saveCredentials only if a successful test has been performed
	private Button saveCredentials;
	private final String title;
	private final String message;
	private String errorMessage;
	private Text errorMessageText;

	public static final int TEST_CREDENTIALS_ID = 133;
	public static final int SAVE_CREDENTIALS_ID = 134;
	public static final String TEST_CREDENTIALS_LABEL = "Test Hub Connection";
	public static final String SAVE_CREDENTIALS_LABEL = "Save Hub Authorization";

	public AuthorizationDialog(final Shell parentShell, final String dialogTitle, final String dialogMessage,
			final String initialUsernameValue, final AuthorizationValidator validator) {
		super(parentShell);
		this.title = dialogTitle;
		this.message = dialogMessage;
		this.validator = validator;
		if (initialUsernameValue == null) {
			usernameInputValue = "";
		} else {
			usernameInputValue = initialUsernameValue;
		}
		passwordInputValue = "";
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
		// what to do if certain button is pressed
		// default: calls okPressed() if ok button pressed
		// default: calls cancelPressed() if cancel button pressed
		// default: ignores all other button presses
		// will need to add custom functionality
		if (buttonId == TEST_CREDENTIALS_ID) {
			validateInput();
		} else if (buttonId == SAVE_CREDENTIALS_ID) {
			// if valid, then saveCredentials()
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		// default: calls getCancelButton, getOkButton to make cancel and ok
		// buttons
		// Will need to add custom buttons
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		testCredentials = createButton(parent, TEST_CREDENTIALS_ID, TEST_CREDENTIALS_LABEL, true);
		saveCredentials = createButton(parent, SAVE_CREDENTIALS_ID, SAVE_CREDENTIALS_LABEL, false);
		// setting focus??
		if (usernameInputValue != null) {
			username.setText(usernameInputValue);
		}
		if (passwordInputValue != null) {
			password.setText(passwordInputValue);
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
		final Label usernameLabel = new Label(composite, SWT.WRAP);
		usernameLabel.setText("Username:");
		usernameLabel.setLayoutData(labelData);
		usernameLabel.setFont(parent.getFont());
		username = new Text(composite, SWT.SINGLE | SWT.BORDER);
		username.setLayoutData(textData);

		final Label passwordLabel = new Label(composite, SWT.WRAP);
		passwordLabel.setText("password:");
		passwordLabel.setLayoutData(labelData);
		passwordLabel.setFont(parent.getFont());
		password = new Text(composite, SWT.SINGLE | SWT.BORDER);
		password.setLayoutData(textData);

		errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		// Set the error message text
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=66292
		setErrorMessage(errorMessage);

		applyDialogFont(composite);
		// add controls to composite as necessary (such as input fields?)
		return composite;
	}

	protected void validateInput() {
		// call AuthorizationValidator.isValid(String username, String password)
		String errorMessage = null;
		if (validator != null) {
			errorMessage = validator.isValid(username.getText(), password.getText());
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

}
