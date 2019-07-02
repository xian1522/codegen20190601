package com.wj.codegen.xml;

public class Attribute implements Comparable<Attribute>{
	
	private String name;
	private String value;
	
	public Attribute(String name,String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getFormattedContent(int indentLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("=\"");
		sb.append(value);
		sb.append("\"");
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public int compareTo(Attribute o) {
		if(this.name == null) {
			return o.getName() == null ? 0 : -1;
		}else {
			if(o.name == null) {
				return 0;
			}else {
				return this.name.compareTo(o.name);
			}
		}
	}

	
	
}

