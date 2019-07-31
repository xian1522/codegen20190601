package com.wj.codegen.generatefile.oracle.xmlmapper.elements;

import com.wj.codegen.xml.Attribute;
import com.wj.codegen.xml.XmlElement;

public class ClassElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("class");
		
		answer.addAttribute(new Attribute("name",introspectedTable.getBaseRecordType()));
		answer.addAttribute(new Attribute("table",introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
		
		parentElement.addElement(answer);
	}


}
