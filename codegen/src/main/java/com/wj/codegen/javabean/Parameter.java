package com.wj.codegen.javabean;

public class Parameter {
	private String name;
	private FullyQualifiedJavaType type;
	private boolean isVarargs;
	
	public Parameter(FullyQualifiedJavaType type,String name) {
		this(type, name, false);
	}

	public Parameter(FullyQualifiedJavaType type, String name, boolean isVarargs) {
		this.name = name;
		this.type = type;
		this.isVarargs = isVarargs;
	}
	
	public String getFormattedContent(CompilationUnit compilationUnit) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(type);
		sb.append(' ');
		if(isVarargs){
			sb.append("... ");
		}
		sb.append(name);
		
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public FullyQualifiedJavaType getType() {
		return type;
	}

	public boolean isVarargs() {
		return isVarargs;
	}
	
}
