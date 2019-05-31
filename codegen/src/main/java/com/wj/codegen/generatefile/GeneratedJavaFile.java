package com.wj.codegen.generatefile;

import com.wj.codegen.javabean.CompilationUnit;

public class GeneratedJavaFile extends GeneratedFile {
	
	private CompilationUnit compilationUnit;
	
	private String fileEncoding;
	
	private JavaFormatter javaFormatter;
	

	public GeneratedJavaFile(CompilationUnit compilationUnit,
			String targetProject,
			String fileEncoding,
			JavaFormatter javaFormatter) {
		super(targetProject);
		this.compilationUnit = compilationUnit;
		this.fileEncoding = fileEncoding;
		this.javaFormatter = javaFormatter;
	}
	
	public GeneratedJavaFile(CompilationUnit compilationUnit,
			String targetProject,
			JavaFormatter javaFormatter) {
		this(compilationUnit, targetProject, null, javaFormatter);
	}

	@Override
	public String getFormattedContent() {
		return javaFormatter.getFormattedConetent(compilationUnit);
	}

	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}
	
	public String getTargetPackage() {
		return compilationUnit.getType().getPackageName();
	}

	@Override
	public String getFileName() {
		return compilationUnit.getType().getShortNameWithoutTypeArguments();
	}
	
	
}
