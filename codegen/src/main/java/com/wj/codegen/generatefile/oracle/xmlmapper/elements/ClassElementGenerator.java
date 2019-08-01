package com.wj.codegen.generatefile.oracle.xmlmapper.elements;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.xml.Attribute;
import com.wj.codegen.xml.XmlElement;

public class ClassElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("class");
		
		answer.addAttribute(new Attribute("name",introspectedTable.getBaseRecordType()));
		answer.addAttribute(new Attribute("table",introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
		
		addPropertyElements(answer);
		
		parentElement.addElement(answer);
	}
	
	private void addPropertyElements(XmlElement answer) {
		for(IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			XmlElement idElement = new XmlElement("id");
			idElement.addAttribute(new Attribute("name",introspectedColumn.getJavaProperty()));
			idElement.addAttribute(new Attribute("type",introspectedColumn.getFullQualifiedJavaType().getFullyQualifiedName()));
			
			//添加column子元素
			addColumnElements(idElement,introspectedColumn);
			
			answer.addElement(idElement);
		}
		
		for(IntrospectedColumn introspectedColumn : introspectedTable.getBaseColumns()) {
			if("LSTMNTDATE".equals(introspectedColumn.getActualColumnName())) {
				XmlElement timestampElement = new XmlElement("timestamp");
				timestampElement.addAttribute(new Attribute("name",introspectedColumn.getJavaProperty()));
				timestampElement.addAttribute(new Attribute("column",introspectedColumn.getActualColumnName()));
				timestampElement.addAttribute(new Attribute("source","db"));
				
				answer.addElement(timestampElement);
			}else {
				XmlElement propertyColumn = new XmlElement("property");
				
				if("CREATEDATE".equals(introspectedColumn.getActualColumnName())) {
					propertyColumn.addAttribute(new Attribute("name",introspectedColumn.getJavaProperty()));
					propertyColumn.addAttribute(new Attribute("generated","insert"));
					propertyColumn.addAttribute(new Attribute("not-null","true"));
					
					addColumnElements(propertyColumn, introspectedColumn);
				}else {
					propertyColumn.addAttribute(new Attribute("name",introspectedColumn.getJavaProperty()));
					propertyColumn.addAttribute(new Attribute("type",introspectedColumn.getFullQualifiedJavaType().getFullyQualifiedName()));
					
					addColumnElements(propertyColumn, introspectedColumn);
				}
				
				answer.addElement(propertyColumn);
			}
			
		}
	}
	
	private void addColumnElements(XmlElement answer,IntrospectedColumn column) {
		XmlElement columnElement = new XmlElement("column");
		columnElement.addAttribute(new Attribute("name",column.getActualColumnName()));
		
		if("CREATEDATE".equals(column.getActualColumnName())) {
			columnElement.addAttribute(new Attribute("sql-type",column.getFullQualifiedJavaType().getFullyQualifiedName()));
			columnElement.addAttribute(new Attribute("default","CURRENT_TIMESTAMP"));
		}else if("BigDecimal".equals(column.getFullQualifiedJavaType().getShortName())){
			String precision = String.valueOf(column.getLength());
			String scale = String .valueOf(column.getScale());
			columnElement.addAttribute(new Attribute("precision",precision));
			columnElement.addAttribute(new Attribute("scale",scale));
		}else {
			columnElement.addAttribute(new Attribute("length",String.valueOf(column.getLength())));
		}
		
		
		answer.addElement(columnElement);
	}


}
