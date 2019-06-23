package com.wj.codegen.javabean;

import com.wj.codegen.util.JavaDomUtil;
import com.wj.codegen.util.OutputUtilities;

public class Field extends JavaElement {
	private String name;
	private FullyQualifiedJavaType type;
	
	public String getFormattedContent(int indentLevel,CompilationUnit compilationUnit) {
		StringBuilder sb = new StringBuilder();
		
		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append(this.getVisibility().getValue());
		
		sb.append(JavaDomUtil.calculateTypeName(compilationUnit, type));
		
		sb.append(' ');
		sb.append(name);
		sb.append(';');
		
		return sb.toString();
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FullyQualifiedJavaType getType() {
		return type;
	}

	public void setType(FullyQualifiedJavaType type) {
		this.type = type;
	}
	
	
	
}
