package com.wj.codegen.generatefile.oracle.javamapper;

import java.util.List;

import com.wj.codegen.generatefile.oracle.AbstractJavaClientGenerator;
import com.wj.codegen.generatefile.oracle.AbstractXmlGenerator;
import com.wj.codegen.javabean.CompilationUnit;

public class SimpleJavaClientGenerator extends AbstractJavaClientGenerator {
	
	public SimpleJavaClientGenerator() {
		super(true);
	}
	public SimpleJavaClientGenerator(boolean requiresXMLGenerator) {
		super(requiresXMLGenerator);
	}

	@Override
	public AbstractXmlGenerator getMatchedXMLGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
		// TODO Auto-generated method stub
		return null;
	}

}
