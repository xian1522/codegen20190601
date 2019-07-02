package com.wj.codegen.generatefile;

import com.wj.codegen.xml.Document;

public class GeneratedXmlFile extends GeneratedFile {
	
	private String fileName;
	private String targetPackage;
	private Document document;
	private XmlFormatter xmlFormatter;

	public GeneratedXmlFile(String targetProject) {
		super(targetProject);
	}

	@Override
	public String getFormattedContent() {
		return xmlFormatter.getFormattedContent(document);
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public String getTargetPackage() {
		return targetPackage;
	}
	
	
}
