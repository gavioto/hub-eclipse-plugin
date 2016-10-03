package com.blackducksoftware.integration.eclipseplugin.dialogs;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.builder.ValidationResult;
import com.blackducksoftware.integration.hub.builder.ValidationResultEnum;
import com.blackducksoftware.integration.hub.builder.ValidationResults;
import com.blackducksoftware.integration.hub.global.GlobalFieldKey;
import com.blackducksoftware.integration.hub.global.HubServerConfig;

public class AuthorizationValidator {

	public String isValid(final String username, final String password) {
		// call hub-common functionality here
		final HubServerConfigBuilder builder = new HubServerConfigBuilder(true);
		builder.setUsername(username);
		builder.setPassword(password);
		builder.setHubUrl("http://eng-hub-valid01.dc1.lan/");
		builder.setTimeout(120);
		builder.setProxyPassword("");
		builder.setProxyPort("");
		builder.setProxyUsername("");
		builder.setProxyHost("");
		builder.setIgnoredProxyHosts("");
		final ValidationResults<GlobalFieldKey, HubServerConfig> results = builder.buildResults();
		if (results.isSuccess()) {
			System.out.println("success");
			final Map<GlobalFieldKey, List<ValidationResult>> resultMap = results.getResultMap();
			final Set<GlobalFieldKey> keyset = resultMap.keySet();
			final Iterator<GlobalFieldKey> keyit = keyset.iterator();
			while (keyit.hasNext()) {
				final List<ValidationResult> resultList = resultMap.get(keyit.next());
				final Iterator<ValidationResult> resultit = resultList.iterator();
				while (resultit.hasNext()) {
					final ValidationResult result = resultit.next();
					final ValidationResultEnum type = result.getResultType();
					if (type == ValidationResultEnum.ERROR) {
						System.out.println("error");
					} else if (type == ValidationResultEnum.OK) {
						System.out.println("ok");
					} else if (type == ValidationResultEnum.WARN) {
						System.out.println("warning");
					} else if (type == null) {
						System.out.println("null");
					} else {
						System.out.println("idk");
					}
				}
			}
		} else {
			System.out.println("failure");
		}
		return null;
	}

}
