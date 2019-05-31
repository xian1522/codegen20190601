package com.wj.codegen.generatefile.callback;

import java.io.File;
import java.util.StringTokenizer;

import com.wj.codegen.exception.ShellException;

public class DefaultShellCallback implements ShellCallback {
	
	private boolean overwrite;
	
	public DefaultShellCallback(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	/**
	 * 获取文件路径
	 */
	public File getDirectory(String targetProject, String targetPackage) throws ShellException {
		File project = new File(targetProject);
		if(!project.isDirectory()) {
			throw new ShellException("getDirectory error");
		}
		
		StringBuilder sb = new StringBuilder();
		//字符串分割，官方推荐用split方法
		StringTokenizer st = new StringTokenizer(targetPackage,".");
		while(st.hasMoreTokens()) {
			sb.append(st.nextToken());
			sb.append(File.separatorChar);
		}
		File directory = new File(project, sb.toString());
		if(!directory.isDirectory()) {
			boolean rc = directory.mkdirs();
			if(!rc) {
				throw new ShellException("getDirectory error");
			}
		}
		return directory;
	}
	
	public boolean isOverwriteEnabled() {
		return this.overwrite;
	}

}
