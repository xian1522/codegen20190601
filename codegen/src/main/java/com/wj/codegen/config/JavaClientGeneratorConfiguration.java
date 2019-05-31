package com.wj.codegen.config;

public class JavaClientGeneratorConfiguration extends TypePropertyHolder {
	private String targetPackage;
	private String implementationPackage;
	private String targetProject;
	
	public String getTargetPackage() {
		return targetPackage;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	public String getImplementationPackage() {
		return implementationPackage;
	}
	public void setImplementationPackage(String implementationPackage) {
		this.implementationPackage = implementationPackage;
	}
	public String getTargetProject() {
		return targetProject;
	}
	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
	
	
	
}


