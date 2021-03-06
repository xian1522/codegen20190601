package com.wj.codegen.generatefile.oracle;

import java.util.List;
import java.util.Properties;

import com.wj.codegen.config.Context;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.generatefile.callback.ProgressCallBack;

public abstract class AbstractGenerator {
	protected Context context;
	protected IntrospectedTable introspectedTable;
	protected List<String> warnings;
	protected ProgressCallBack progressCallback;
	
	public String getRootClass() {
		String rootClass = introspectedTable
				.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_CLASS);
		if(rootClass == null) {
			Properties properties = context.getJavaClientGeneratorConfiguration().getProperties();
			rootClass = properties.getProperty(PropertyRegistry.ANY_ROOT_CLASS);
		}
		return rootClass;
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public IntrospectedTable getIntrospectedTable() {
		return introspectedTable;
	}
	public void setIntrospectedTable(IntrospectedTable introspectedTable) {
		this.introspectedTable = introspectedTable;
	}
	public List<String> getWarnings() {
		return warnings;
	}
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	public ProgressCallBack getProgressCallback() {
		return progressCallback;
	}
	public void setProgressCallback(ProgressCallBack progressCallback) {
		this.progressCallback = progressCallback;
	}
	
	
}
