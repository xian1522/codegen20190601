package com.wj.codegen.generatefile.oracle.xmlmapper.elements;

import com.wj.codegen.generatefile.oracle.AbstractGenerator;
import com.wj.codegen.xml.XmlElement;

public abstract class AbstractXmlElementGenerator extends AbstractGenerator {
	
	public abstract void addElements(XmlElement parentElement);
}
