package com.wj.codegen.generatefile;

import com.wj.codegen.config.Context;
import com.wj.codegen.xml.Document;

public interface XmlFormatter {
	public void setContext(Context context);
	public String getFormattedContent(Document document);
}
