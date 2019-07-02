package com.wj.codegen.xml;

public abstract class Element {
	
	public Element() {
		super();
	}
	
	public abstract String getFormattedContent(int indentLevel);
}
