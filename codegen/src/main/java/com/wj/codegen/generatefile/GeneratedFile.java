package com.wj.codegen.generatefile;

public abstract class GeneratedFile {
	
	protected String targetProject;
	
	public GeneratedFile(String targetProject) {
		this.targetProject = targetProject;
	}
	
	public abstract String getFormattedContent();
	
	/**
	 * 获取不带路径的文件名
	* @Description
	* @user w.j
	* @date 2019年5月22日 下午8:25:21
	* @throws
	 */
	public abstract String getFileName();
	
	@Override
	public String toString() {
		return getFormattedContent();
	}
	
	public String getTargetProject() {
		return targetProject;
	}
}
