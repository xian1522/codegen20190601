package com.wj.codegen.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlElement extends Element {
	
	private List<Attribute> attributes;
	private List<Element> elements;
	
	private String name;
	
	public XmlElement(String name) {
		this.attributes = new ArrayList<Attribute>();
		this.elements = new ArrayList<Element>();
		this.name = name;
	}
	

	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}


	public List<Element> getElements() {
		return elements;
	}
	public void addElement(Element element) {
		this.elements.add(element);
	}

	public String getName() {
		return name;
	}



	@Override
	public String getFormattedContent(int indentLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(name);
		for(Attribute attribute : this.attributes) {
			sb.append(" ");
			sb.append(attribute.getFormattedContent(indentLevel));
		}
		for(Element element : this.elements) {
			element.getFormattedContent(indentLevel);
		}
		sb.append("</");
		sb.append(name);
		sb.append(">");
		return sb.toString();
	}

}
