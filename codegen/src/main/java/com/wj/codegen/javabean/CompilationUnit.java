package com.wj.codegen.javabean;

import java.util.Set;

public interface CompilationUnit {
	
	String getFormattedContent();
	
	Set<FullyQualifiedJavaType> getImportTypes();
	
	Set<String> getStaticImports();
	
	boolean isJavaInterface();
	
	boolean isJavaEnumeration();
	
	Set<FullyQualifiedJavaType> getSuperInterfaceTypes();
	
	FullyQualifiedJavaType getType();
}
