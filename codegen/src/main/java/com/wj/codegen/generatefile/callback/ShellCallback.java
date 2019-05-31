package com.wj.codegen.generatefile.callback;

import java.io.File;

import com.wj.codegen.exception.ShellException;

public interface ShellCallback {
	
	File getDirectory(String targetProject,String targetPackage) throws ShellException;
}
