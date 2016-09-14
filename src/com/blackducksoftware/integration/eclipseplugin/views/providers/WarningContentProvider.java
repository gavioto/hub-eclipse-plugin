package com.blackducksoftware.integration.eclipseplugin.views.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
// import com.blackducksoftware.integration.eclipseplugin.internal.Warning;

public class WarningContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		// String component;
		
		return (Object []) inputElement;
		
		/*
		String matchCount;
		String matchType;
		String usage;
		String license;
		String securityRisk;
		String operationalRisk;
		
		System.out.println("class type in WarningContentProvider.java: " + inputElement.getClass());;
		
		if (inputElement instanceof Warning) {
			
			/*
			if (((Warning) inputElement).hasComponent()) {
				component = ((Warning) inputElement).getComponent();
			} else {
				component = "unknown component";
			}
			
			
			matchCount = ((Warning) inputElement).getMatchCount();

			if (((Warning) inputElement).hasMatchType()) {
				matchType = ((Warning) inputElement).getMatchType();
			} else {
				matchType = "unknown match type";
			}
			
			if (((Warning) inputElement).hasUsage()) {
				usage = ((Warning) inputElement).getUsage();
			} else {
				usage = "unknown usage";
			}
			
			if (((Warning) inputElement).hasLicense()) {
				license = ((Warning) inputElement).getLicense();
			} else {
				license = "unknown license";
			}
			
			if (((Warning) inputElement).hasSecurityRisk()) {
				securityRisk = ((Warning) inputElement).getSecurityRisk();
			} else {
				securityRisk = "unknown security risk";
			}
			
			if (((Warning) inputElement).hasOperationalRisk()) {
				operationalRisk = ((Warning) inputElement).getOperationalRisk();
			} else {
				operationalRisk = "unknown operational risk";
			}
			
			Object [] vals = {matchCount, matchType, usage, license, securityRisk, operationalRisk};
			return vals;
			
		} else {
			System.err.println("unknown input type " + inputElement.getClass());
			Object unknown = new Warning("unknown", 0, "unknown", "unknown", "unknown", "unknown", "unknown");
			Object [] unknownArray = {unknown};
			return unknownArray;
		}
		*/
	}
	
	
}
