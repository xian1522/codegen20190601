package com.wj.codegen.config;

public class SqlMapGeneratorConfiguration extends PropertyHolder {
	private String targetPackage;
	
	private String targetProject;
	
	public SqlMapGeneratorConfiguration() {
		super();
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
	
	
}
