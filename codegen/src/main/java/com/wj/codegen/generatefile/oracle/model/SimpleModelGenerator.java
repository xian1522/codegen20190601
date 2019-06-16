package com.wj.codegen.generatefile.oracle.model;

import java.util.ArrayList;
import java.util.List;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.generatefile.oracle.AbstractJavaGenerator;
import com.wj.codegen.generatefile.oracle.RootClassInfo;
import com.wj.codegen.javabean.CompilationUnit;
import com.wj.codegen.javabean.Field;
import com.wj.codegen.javabean.FullyQualifiedJavaType;
import com.wj.codegen.javabean.JavaVisibility;
import com.wj.codegen.javabean.Method;
import com.wj.codegen.javabean.Parameter;
import com.wj.codegen.javabean.TopLevelClass;
import com.wj.codegen.util.JavaBeansUtil;

public class SimpleModelGenerator extends AbstractJavaGenerator {
	
	public SimpleModelGenerator() {
		
	}
	
	@Override
	public List<CompilationUnit> getCompilationUnits(){
	//	FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		TopLevelClass topLevelClass = new TopLevelClass(type);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		
		FullyQualifiedJavaType superClass = getSuperClass();
		if(superClass != null) {
			topLevelClass.setSuperClass(superClass);
			topLevelClass.addImportedTypes(superClass);
		}
		
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
		
		if(introspectedTable.isConstructorBased()) {
			this.addParameterizedConstructor(topLevelClass);
		
			if(!introspectedTable.isImmutable()) {
				addDefaultConstructor(topLevelClass);
			}
		}
		
		String rootClass = getRootClass();
		for(IntrospectedColumn introspectedColumn : introspectedColumns) {
			if(RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
				continue;
			}
			
			Field field = JavaBeansUtil.getJavaBeanField(introspectedColumn,context,introspectedTable);
			topLevelClass.addField(field);
			topLevelClass.addImportedTypes(field.getType());
			
			Method method = JavaBeansUtil.getJavaBeansGetter(introspectedColumn,context,
					introspectedTable);
			topLevelClass.addMethod(method);
			
			if(!introspectedTable.isImmutable()) {
				method = JavaBeansUtil.getJavaBeanSetter(introspectedColumn,context,introspectedTable);
				topLevelClass.addMethod(method);
			}
			
		}
		
		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		answer.add(topLevelClass);
		
		return answer;
		
	}
	
	private void addDefaultConstructor(TopLevelClass topLevelClass) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setConstructor(true);
		method.setName(topLevelClass.getType().getShortName());
		method.addBodyLine("super();");
		topLevelClass.addMethod(method);
	}
	
	private void addParameterizedConstructor(TopLevelClass topLevelClass) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setConstructor(true);
		method.setName(topLevelClass.getType().getShortName());
		
		List<IntrospectedColumn> constructorColumns = introspectedTable.getAllColumns();
		for(IntrospectedColumn introspectedColumn : constructorColumns) {
			method.addParameter(new Parameter(introspectedColumn.getFullQualifiedJavaType(),
					introspectedColumn.getJavaProperty()));
		}
		
		StringBuilder sb = new StringBuilder();
		if(introspectedTable.getRules().generatePrimaryKeyClass()) {
			boolean comma = false;
			sb.append("super(");
			for(IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
				if(comma) {
					sb.append(", ");
				}else {
					comma = true;
				}
				sb.append(introspectedColumn.getJavaProperty());
			}
			sb.append(");");
			method.addBodyLine(sb.toString());
		}
		
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
		for(IntrospectedColumn introspectedColumn : introspectedColumns) {
			sb.setLength(0);
			sb.append("this. ");
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" = ");
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(";");
			method.addBodyLine(sb.toString());
		}
		
		topLevelClass.addMethod(method);
	}
	
	private FullyQualifiedJavaType getSuperClass() {
		FullyQualifiedJavaType superClass;
		String rootClass = getRootClass();
		if(rootClass != null) {
			superClass = new FullyQualifiedJavaType(rootClass);
		}else {
			superClass = null;
		}
		return superClass;
	}
}
