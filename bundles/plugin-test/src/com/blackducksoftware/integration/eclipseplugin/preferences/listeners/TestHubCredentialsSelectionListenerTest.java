package com.blackducksoftware.integration.eclipseplugin.preferences.listeners;

import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.blackducksoftware.integration.eclipseplugin.internal.AuthorizationResponse;
import com.blackducksoftware.integration.eclipseplugin.internal.AuthorizationValidator;

@RunWith(MockitoJUnitRunner.class)
public class TestHubCredentialsSelectionListenerTest {

    @Mock
    StringFieldEditor hubUsername, hubURL, proxyUsername, proxyHost, ignoredProxyHosts;

    @Mock
    IntegerFieldEditor hubTimeout, proxyPort;

    @Mock
    Text hubPassword, proxyPassword, connectionMessageText;

    @Mock
    AuthorizationValidator validator;

    @Mock
    SelectionEvent e;

    private final String MESSAGE = "message";

    @Test
    public void testWidgetSelected() {
        Mockito.when(validator.validateCredentials(null, null, null, null, null, null, null, null, null))
                .thenReturn(new AuthorizationResponse(MESSAGE));
        final TestHubCredentialsSelectionListener listener = new TestHubCredentialsSelectionListener(hubUsername,
                hubPassword, hubURL, hubTimeout, proxyUsername, proxyPassword, proxyHost, proxyPort, ignoredProxyHosts,
                connectionMessageText, validator);
        listener.widgetSelected(e);
        Mockito.verify(connectionMessageText, Mockito.times(1)).setText(MESSAGE);
    }

    @Test
    public void testWidgetDefaultSelected() {
        Mockito.when(validator.validateCredentials(null, null, null, null, null, null, null, null, null))
                .thenReturn(new AuthorizationResponse(MESSAGE));
        final TestHubCredentialsSelectionListener listener = new TestHubCredentialsSelectionListener(hubUsername,
                hubPassword, hubURL, hubTimeout, proxyUsername, proxyPassword, proxyHost, proxyPort, ignoredProxyHosts,
                connectionMessageText, validator);
        listener.widgetDefaultSelected(e);
        Mockito.verify(connectionMessageText, Mockito.times(1)).setText(MESSAGE);
    }

}
