package com.wj.codegen.xml;

import java.util.ArrayList;
import java.util.List;

import com.wj.codegen.util.OutputUtilities;

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
		OutputUtilities.xmlIndent(sb, indentLevel);
		sb.append("<");
		sb.append(name);
		for(Attribute attribute : this.attributes) {
			sb.append(" ");
			sb.append(attribute.getFormattedContent(indentLevel));
		}
		if(elements.size() > 0) {
			sb.append(">");
			for(Element element : this.elements) {
				OutputUtilities.newLine(sb);
				sb.append(element.getFormattedContent(indentLevel + 1));
			}
			OutputUtilities.newLine(sb);
			OutputUtilities.xmlIndent(sb, indentLevel);
			sb.append("</");
			sb.append(name);
			sb.append(">");
		}else {
			sb.append("/>");
		}
		return sb.toString();
	}

}
