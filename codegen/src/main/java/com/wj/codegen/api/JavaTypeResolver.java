package com.wj.codegen.api;

import java.util.List;
import java.util.Properties;

import com.wj.codegen.config.Context;
import com.wj.codegen.javabean.FullyQualifiedJavaType;

public interface JavaTypeResolver {
	
	void setWarnings(List<String> warnings);
	
	void addConfigurationProperties(Properties properties);
	
	void setContext(Context context);
	
	FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn);

	String calculateJdbcTypeName(IntrospectedColumn introspectedColumn);
}
