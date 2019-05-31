package com.wj.codegen.config;

public abstract class TypePropertyHolder extends PropertyHolder{
	
	private String configurationType;
	
	
	public String getConfigurationType() {
		return configurationType;
	}
	
	public void setConfigurationType(String configurationType) {
		if(!"DEFAULT".equalsIgnoreCase(configurationType)) {
			this.configurationType = configurationType;
		}
	}
}
