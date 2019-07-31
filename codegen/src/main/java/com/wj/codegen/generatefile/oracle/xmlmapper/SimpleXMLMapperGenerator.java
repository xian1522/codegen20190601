package com.wj.codegen.generatefile.oracle.xmlmapper;

import com.wj.codegen.config.FullyQualifiedTable;
import com.wj.codegen.generatefile.oracle.AbstractXmlGenerator;
import com.wj.codegen.xml.Document;
import com.wj.codegen.xml.XmlElement;

public class SimpleXMLMapperGenerator extends AbstractXmlGenerator {
	
	public SimpleXMLMapperGenerator() {
		super();
	}
	
	protected XmlElement getSqlMapElement() {
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		XmlElement answer = new XmlElement("hibernate-mapping");
		
		return answer;
	}

	@Override
	public Document getDocument() {
		// TODO Auto-generated method stub
		return null;
	}

}
