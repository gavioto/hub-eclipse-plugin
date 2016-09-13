package com.blackducksoftware.integration.eclipseplugin.internal;

public class Warning {
	
	private String component;
	private int matchCount;
	private String matchType;
	private String usage;
	private String license;
	private String securityRisk;
	private String operationalRisk;
	
	public Warning(String component, int matchCount, String matchType, String usage,
				   String license, String securityRisk, String operationalRisk) {
		this.component = component;
		this.matchCount = matchCount;
		this.matchType = matchType;
		this.usage = usage;
		this.license = license;
		this.securityRisk = securityRisk;
		this.operationalRisk = operationalRisk;
	}
	
	public void setComponent(String component) {
		this.component = component;
	}
	
	public String getComponent() {
		return this.component;
	}
	
	public boolean hasComponent() {
		return component != null;
	}
	
	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}
	
	public String getMatchCount() {
		return "" + this.matchCount + " Matches";
	}
	
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	
	public String getMatchType() {
		return this.matchType;
	}
	
	public boolean hasMatchType() {
		return matchType != null;
	}
	
	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	public String getUsage() {
		return this.usage;
	}
	
	public boolean hasUsage() {
		return usage != null;
	}
	
	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getLicense() {
		return this.license;
	}
	
	public boolean hasLicense() {
		return license != null;
	}
	
	public void setSecurityRisk(String securityRisk) {
		this.securityRisk = securityRisk;
	}
	
	public String getSecurityRisk() {
		return this.securityRisk;
	}
	
	public boolean hasSecurityRisk() {
		return securityRisk != null;
	}
	
	public void setOperationalRisk(String operationalRisk) {
		this.operationalRisk = operationalRisk;
	}
	
	public String getOperationalRisk() {
		return operationalRisk;
	}
	
	public boolean hasOperationalRisk() {
		return operationalRisk != null;
	}
}
