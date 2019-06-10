package com.wj.codegen.generatefile.oracle;

import java.util.List;

import com.wj.codegen.javabean.CompilationUnit;

public abstract class AbstractJavaGenerator extends AbstractGenerator {
	public abstract List<CompilationUnit> getCompilationUnits();
}
