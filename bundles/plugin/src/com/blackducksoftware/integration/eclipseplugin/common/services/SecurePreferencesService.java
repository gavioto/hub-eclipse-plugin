package com.blackducksoftware.integration.eclipseplugin.common.services;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;

public class SecurePreferencesService {

	private final String nodeName;
	private final ISecurePreferences root;

	public SecurePreferencesService(final String nodeName, final ISecurePreferences root) {
		this.nodeName = nodeName;
		this.root = root;
	}

	public String getSecurePreference(final String name) {
		final ISecurePreferences nodeToAccess = root.node(nodeName);
		try {
			return nodeToAccess.get(name, "");
		} catch (final StorageException e) {
			return "";
		}
	}

	public boolean saveSecurePreference(final String name, final String value, final boolean encrypt) {
		boolean saveSuccess;
		final ISecurePreferences nodeToAccess = root.node(nodeName);
		try {
			nodeToAccess.put(name, value, encrypt);
			saveSuccess = true;
		} catch (final StorageException e) {
			saveSuccess = false;
		}
		return saveSuccess;
	}
}
