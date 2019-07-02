package com.wj.codegen.generatefile;

import com.wj.codegen.config.Context;
import com.wj.codegen.xml.Document;

public class XmlDefaultFormatter implements XmlFormatter {
	
	protected Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	public String getFormattedContent(Document document) {
		return document.getFormattedContent();
	}

}
