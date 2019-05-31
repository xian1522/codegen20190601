package com.wj.codegen.javabean;

import java.util.List;

public abstract class JavaElement {
	private List<String> annotations;
	private List<String> javaDocLines;
	
	private JavaVisibility visibility = JavaVisibility.DEFAULT;
	
	public List<String> getAnnotations() {
		return annotations;
	}
	public List<String> getJavaDocLines(){
		return javaDocLines;
	}

	public JavaVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(JavaVisibility visibility) {
		this.visibility = visibility;
	}
	
	
}
