package com.wj.codegen.config;

import java.util.Properties;

public abstract class PropertyHolder {
	private Properties properties;
	
	public PropertyHolder() {
		properties = new Properties();
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
	
	public void addProperty(String name,String value) {
		this.properties.put(name, value);
	}
	
}
