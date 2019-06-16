package com.wj.codegen.generatefile.oracle.model;

import java.util.ArrayList;
import java.util.List;

import com.wj.codegen.config.FullyQualifiedTable;
import com.wj.codegen.generatefile.oracle.AbstractJavaGenerator;
import com.wj.codegen.javabean.CompilationUnit;
import com.wj.codegen.javabean.Field;
import com.wj.codegen.javabean.FullyQualifiedJavaType;
import com.wj.codegen.javabean.JavaVisibility;
import com.wj.codegen.javabean.Method;
import com.wj.codegen.javabean.TopLevelClass;

public class ExampleGenerator extends AbstractJavaGenerator {
	public ExampleGenerator() {
		super();
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		//commentGenerator undone
		
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getExampleType());
		TopLevelClass topLevelClass = new TopLevelClass(type);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		//commentGenerator.addJavaFileComment(topLevelClass);
		
		//add default constructor
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setConstructor(true);
		method.setName(type.getShortName());
		
		topLevelClass.addMethod(method);
		
		Field field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setType(type);
		
		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		answer.add(topLevelClass);
		return answer;
		
	}
}
