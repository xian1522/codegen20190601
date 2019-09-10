package com.wj.codegen.generatefile;

import com.wj.codegen.xml.Document;

public class GeneratedXmlFile extends GeneratedFile {
	
	private String fileName;
	private String targetPackage;
	private Document document;
	private XmlFormatter xmlFormatter;
	private boolean isMergeable;
	

	public GeneratedXmlFile(String targetProject) {
		super(targetProject);
	}
	
	public GeneratedXmlFile(Document document,String fileName,String targetPackage,
			String targetProject,boolean isMergeable,XmlFormatter xmlFormatter) {
		super(targetProject);
		this.document = document;
		this.fileName = fileName;
		this.targetPackage = targetPackage;
		this.isMergeable = isMergeable;
		this.xmlFormatter = xmlFormatter;
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

	public boolean isMergeable() {
		return isMergeable;
	}
	
	
}
