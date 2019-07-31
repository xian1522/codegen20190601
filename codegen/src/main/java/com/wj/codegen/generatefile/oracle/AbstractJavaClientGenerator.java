package com.wj.codegen.generatefile.oracle;

/**
 * 配置XML生成器
* @Description 
* @author w.j
* @date 2019年7月31日 下午9:02:04
 */
public abstract class AbstractJavaClientGenerator extends AbstractJavaGenerator {
	
	private boolean requiresXMLGenerator;
	
	public AbstractJavaClientGenerator(boolean requiresXMLGenerator) {
		this.requiresXMLGenerator = requiresXMLGenerator;
	}
	
	public boolean requiresXMLGenerator() {
		return requiresXMLGenerator;
	}
	
	public abstract AbstractXmlGenerator getMatchedXMLGenerator();

}
