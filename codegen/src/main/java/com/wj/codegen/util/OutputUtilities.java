package com.wj.codegen.util;

import java.util.Set;
import java.util.TreeSet;

import com.wj.codegen.javabean.FullyQualifiedJavaType;

public class OutputUtilities {
	private static final String lineSeparator;
	static {
		String ls = System.getProperty("line.separator");
		if(ls == null) {
			ls = "\n";
		}
		lineSeparator = ls;
	}
	
	public static void javaIndent(StringBuilder sb,int indentLevel) {
		for(int i = 0; i<indentLevel; i++) {
			sb.append("		");
		}
	}
	
	public static void newLine(StringBuilder sb) {
		sb.append(lineSeparator);
	}
	
	public static Set<String> calculateImports(Set<FullyQualifiedJavaType> importedTypes){
		StringBuilder sb = new StringBuilder();
		Set<String> importStrings = new TreeSet<String>();
		for(FullyQualifiedJavaType fqjt : importedTypes) {
			for(String importString : fqjt.getImportList()) {
				sb.setLength(0);
				sb.append("import ");
				sb.append(importString);
				sb.append(';');
				importStrings.add(sb.toString());
			}
		}
		
		return importStrings;
	}
}
