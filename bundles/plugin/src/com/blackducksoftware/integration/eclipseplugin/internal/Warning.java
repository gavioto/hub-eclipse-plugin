package com.blackducksoftware.integration.eclipseplugin.internal;

/*
 * Class representing an analysis of a component from the Hub
 */
public class Warning {

	private final String component;
	private final int matchCount;
	private final String matchType;
	private final String usage;
	private final String license;
	private final String securityRisk;
	private final String operationalRisk;

	public static int COMPONENT_COLUMN_INDEX = 0;
	public static int MATCH_COUNT_COLUMN_INDEX = 1;
	public static int MATCH_TYPE_COLUMN_INDEX = 2;
	public static int USAGE_COLUMN_INDEX = 3;
	public static int LICENSE_COLUMN_INDEX = 4;
	public static int SECURITY_RISK_COLUMN_INDEX = 5;
	public static int OPERATIONAL_RISK_COLUMN_INDEX = 6;

	public Warning(final String component, final int matchCount, final String matchType, final String usage,
			final String license, final String securityRisk, final String operationalRisk) {
		this.component = component;
		this.matchCount = matchCount;
		this.matchType = matchType;
		this.usage = usage;
		this.license = license;
		this.securityRisk = securityRisk;
		this.operationalRisk = operationalRisk;
	}

	public String getComponent() {
		return this.component;
	}

	public boolean hasComponent() {
		return component != null;
	}

	public String getMatchCount() {
		return "" + this.matchCount + " Matches";
	}

	public String getMatchType() {
		return this.matchType;
	}

	public boolean hasMatchType() {
		return matchType != null;
	}

	public String getUsage() {
		return this.usage;
	}

	public boolean hasUsage() {
		return usage != null;
	}

	public String getLicense() {
		return this.license;
	}

	public boolean hasLicense() {
		return license != null;
	}

	public String getSecurityRisk() {
		return this.securityRisk;
	}

	public boolean hasSecurityRisk() {
		return securityRisk != null;
	}

	public String getOperationalRisk() {
		return operationalRisk;
	}

	public boolean hasOperationalRisk() {
		return operationalRisk != null;
	}
}
