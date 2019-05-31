package com.wj.codegen.generatefile;

import com.wj.codegen.config.Context;
import com.wj.codegen.javabean.CompilationUnit;

public class DefaultJavaFormatter implements JavaFormatter {
	
	protected Context context;
	
	public String getFormattedConetent(CompilationUnit compilationUnit) {
		return compilationUnit.getFormattedContent();
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
