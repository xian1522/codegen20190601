package com.wj.codegen.generatefile;

import com.wj.codegen.config.Context;
import com.wj.codegen.javabean.CompilationUnit;

public interface JavaFormatter {
	String getFormattedConetent(CompilationUnit compilationUnit);
	void setContext(Context context);
}
