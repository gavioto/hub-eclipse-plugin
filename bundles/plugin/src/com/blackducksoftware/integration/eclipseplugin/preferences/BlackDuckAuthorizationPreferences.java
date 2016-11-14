package com.blackducksoftware.integration.eclipseplugin.preferences;

import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.blackducksoftware.integration.eclipseplugin.common.constants.PreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNames;
import com.blackducksoftware.integration.eclipseplugin.common.constants.SecurePreferenceNodes;
import com.blackducksoftware.integration.eclipseplugin.common.services.HubRestConnectionService;
import com.blackducksoftware.integration.eclipseplugin.common.services.SecurePreferencesService;
import com.blackducksoftware.integration.eclipseplugin.internal.AuthorizationResponse;
import com.blackducksoftware.integration.eclipseplugin.internal.AuthorizationValidator;
import com.blackducksoftware.integration.eclipseplugin.preferences.listeners.TestHubCredentialsSelectionListener;
import com.blackducksoftware.integration.eclipseplugin.startup.Activator;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;

public class BlackDuckAuthorizationPreferences extends PreferencePage implements IWorkbenchPreferencePage {

    private SecurePreferencesService securePrefService;

    private StringFieldEditor hubUsername;

    private Text hubPassword; // Encrypt!

    private StringFieldEditor hubURL;

    private IntegerFieldEditor hubTimeout;

    private StringFieldEditor proxyUsername;

    private Text proxyPassword; // Encrypt!

    private StringFieldEditor proxyHost;

    private IntegerFieldEditor proxyPort;

    private StringFieldEditor ignoredProxyHosts;

    private Button testHubCredentials;

    private Text connectionMessageText;

    private AuthorizationValidator validator;

    private final String HUB_USERNAME_LABEL = "Hub Username";

    private final String HUB_PASSWORD_LABEL = "Hub Password";

    private final String HUB_URL_LABEL = "Hub Instance URL";

    private final String HUB_TIMEOUT_LABEL = "Hub Timeout in Seconds";

    private final String PROXY_USERNAME_LABEL = "Proxy Username";

    private final String PROXY_PASSWORD_LABEL = "Proxy Password";

    private final String PROXY_HOST_LABEL = "Proxy Host";

    private final String PROXY_PORT_LABEL = "Proxy Port";

    private final String IGNORED_PROXY_HOSTS_LABEL = "Ignored Proxy Hosts";

    private final String TEST_HUB_CREDENTIALS_TEXT = "Test Hub Credentials";

    @Override
    public void init(final IWorkbench workbench) {
        securePrefService = new SecurePreferencesService(SecurePreferenceNodes.BLACK_DUCK,
                SecurePreferencesFactory.getDefault());
        validator = new AuthorizationValidator(new HubRestConnectionService(), new HubServerConfigBuilder());
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected Control createContents(final Composite parent) {

        final Composite authComposite = new Composite(parent, SWT.LEFT);
        authComposite.setLayout(new GridLayout());
        authComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        final GridData labelData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER);
        labelData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        final GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);

        hubUsername = new StringFieldEditor(PreferenceNames.HUB_USERNAME, HUB_USERNAME_LABEL, authComposite);
        performStringFieldEditorSetup(hubUsername);
        createLabel(parent, authComposite, labelData, HUB_PASSWORD_LABEL);
        hubPassword = new Text(authComposite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
        hubPassword.setLayoutData(textData);
        hubPassword.setText(securePrefService.getSecurePreference(SecurePreferenceNames.HUB_PASSWORD));
        hubURL = new StringFieldEditor(PreferenceNames.HUB_URL, HUB_URL_LABEL, authComposite);
        performStringFieldEditorSetup(hubURL);
        hubTimeout = new IntegerFieldEditor(PreferenceNames.HUB_TIMEOUT, HUB_TIMEOUT_LABEL, authComposite);
        performStringFieldEditorSetup(hubTimeout);
        proxyUsername = new StringFieldEditor(PreferenceNames.PROXY_USERNAME, PROXY_USERNAME_LABEL, authComposite);
        performStringFieldEditorSetup(proxyUsername);
        createLabel(parent, authComposite, labelData, PROXY_PASSWORD_LABEL);
        proxyPassword = new Text(authComposite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
        proxyPassword.setLayoutData(textData);
        proxyPassword.setText(securePrefService.getSecurePreference(SecurePreferenceNames.PROXY_PASSWORD));
        proxyHost = new StringFieldEditor(PreferenceNames.PROXY_HOST, PROXY_HOST_LABEL, authComposite);
        performStringFieldEditorSetup(proxyHost);
        proxyPort = new IntegerFieldEditor(PreferenceNames.PROXY_PORT, PROXY_PORT_LABEL, authComposite);
        performStringFieldEditorSetup(proxyPort);
        ignoredProxyHosts = new StringFieldEditor(PreferenceNames.IGNORED_PROXY_HOSTS, IGNORED_PROXY_HOSTS_LABEL,
                authComposite);
        performStringFieldEditorSetup(ignoredProxyHosts);

        testHubCredentials = new Button(authComposite, SWT.PUSH);
        testHubCredentials.setText(TEST_HUB_CREDENTIALS_TEXT);

        connectionMessageText = new Text(authComposite, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        connectionMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        connectionMessageText
                .setBackground(connectionMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        connectionMessageText.setText("\n");

        testHubCredentials.addSelectionListener(
                new TestHubCredentialsSelectionListener(hubUsername, hubPassword, hubURL, hubTimeout, proxyUsername,
                        proxyPassword, proxyHost, proxyPort, ignoredProxyHosts, connectionMessageText,
                        validator));

        return authComposite;
    }

    private void storeValues() {
        final IPreferenceStore prefStore = getPreferenceStore();
        final StringFieldEditor[] editors = new StringFieldEditor[] { hubUsername, hubURL, hubTimeout, proxyUsername,
                proxyHost, proxyPort, ignoredProxyHosts };
        for (final StringFieldEditor editor : editors) {
            prefStore.setValue(editor.getPreferenceName(), editor.getStringValue());
        }
        securePrefService.saveSecurePreference(SecurePreferenceNames.HUB_PASSWORD, hubPassword.getText(), true);
        securePrefService.saveSecurePreference(SecurePreferenceNames.PROXY_PASSWORD, proxyPassword.getText(), true);
        AuthorizationResponse authResponse = validator.validateCredentials(hubUsername.getStringValue(), hubPassword.getText(), hubURL.getStringValue(),
                proxyUsername.getStringValue(), proxyPassword.getText(), proxyPort.getStringValue(), proxyHost.getStringValue(),
                ignoredProxyHosts.getStringValue(), hubTimeout.getStringValue());
        Activator.getDefault().updateHubConnection(authResponse.getConnection());
    }

    private void createLabel(final Composite parent, final Composite composite, final GridData data,
            final String labelText) {
        final Label label = new Label(composite, SWT.WRAP);
        label.setText(labelText);
        label.setLayoutData(data);
        label.setFont(parent.getFont());
    }

    private void performStringFieldEditorSetup(final StringFieldEditor editor) {
        editor.setPage(this);
        editor.setPreferenceStore(getPreferenceStore());
        editor.load();
    }

    @Override
    public void performApply() {
        storeValues();
    }

    @Override
    public boolean performOk() {
        storeValues();
        if (super.performOk()) {
            return true;
        } else {
            return false;
        }
    }

}
