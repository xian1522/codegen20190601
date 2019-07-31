package com.wj.codegen.generatefile.oracle.javamapper;

import java.util.List;

import com.wj.codegen.generatefile.oracle.AbstractJavaClientGenerator;
import com.wj.codegen.generatefile.oracle.AbstractXmlGenerator;
import com.wj.codegen.javabean.CompilationUnit;

public class JavaMapperGenerator extends AbstractJavaClientGenerator {

	public JavaMapperGenerator() {
		super(true);
	}
	
	public JavaMapperGenerator(boolean requiresXMLGenerator) {
		super(requiresXMLGenerator);
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractXmlGenerator getMatchedXMLGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

}
