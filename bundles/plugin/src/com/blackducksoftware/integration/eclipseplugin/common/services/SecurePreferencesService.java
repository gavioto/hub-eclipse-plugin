package com.blackducksoftware.integration.eclipseplugin.common.services;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;


public class SecurePreferencesService {
	
	private String nodeName;
	private ISecurePreferences root;
	
	public SecurePreferencesService(String nodeName, ISecurePreferences root) {
		this.nodeName = nodeName;
		this.root = root;
	}
	
	public String getSecurePreference(String name) {
		final ISecurePreferences nodeToAccess = root.node(nodeName);
		try {
			return nodeToAccess.get(name, "");
		} catch (StorageException e) {
			return "";
		}
	}
	
	public boolean saveSecurePreference(String name, String value, boolean encrypt) {
		boolean saveSuccess;
		final ISecurePreferences nodeToAccess = root.node(nodeName);
		try {
			nodeToAccess.put(name, value, encrypt);
			saveSuccess = true;
		} catch (StorageException e) {
			saveSuccess = false;
		}
		return saveSuccess;
	}
}
