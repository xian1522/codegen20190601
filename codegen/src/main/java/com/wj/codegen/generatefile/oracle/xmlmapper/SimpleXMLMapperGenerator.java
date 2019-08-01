package com.wj.codegen.generatefile.oracle.xmlmapper;

import com.wj.codegen.config.FullyQualifiedTable;
import com.wj.codegen.generatefile.oracle.AbstractXmlGenerator;
import com.wj.codegen.generatefile.oracle.XmlConstants;
import com.wj.codegen.generatefile.oracle.xmlmapper.elements.AbstractXmlElementGenerator;
import com.wj.codegen.generatefile.oracle.xmlmapper.elements.ClassElementGenerator;
import com.wj.codegen.xml.Document;
import com.wj.codegen.xml.XmlElement;

public class SimpleXMLMapperGenerator extends AbstractXmlGenerator {
	
	public SimpleXMLMapperGenerator() {
		super();
	}
	
	/**
	*  构建XML元素对象
	* @Description
	* @user w.j
	* @date 2019年8月1日 下午11:25:45
	* @throws
	*/
	protected XmlElement getSqlMapElement() {
		
		XmlElement answer = new XmlElement("hibernate-mapping");
		
		AbstractXmlElementGenerator elementGenerator = new ClassElementGenerator();
		elementGenerator.addElements(answer);
		
		return answer;
	}

	@Override
	public Document getDocument() {
		Document document = new Document(
				XmlConstants.HIBERNATE_PUBLIC_ID,
				XmlConstants.HIBERNATE_SYSTEM_ID);
		
		document.setRootElement(getSqlMapElement());
		return document;
	}

}
