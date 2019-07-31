package com.wj.codegen.generatefile.internal.rules;

public interface Rules {
	
	boolean generateInsert();
	
	boolean generateExampleClass();

	boolean generatePrimaryKeyClass();
	
	boolean generatedJavaClient();
}
