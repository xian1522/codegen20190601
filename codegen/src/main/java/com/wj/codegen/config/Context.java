package com.wj.codegen.config;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.generatefile.GeneratedJavaFile;
import com.wj.codegen.generatefile.GeneratedXmlFile;
import com.wj.codegen.generatefile.callback.ProgressCallBack;
import com.wj.codegen.generatefile.internal.ObjectFactory;
import com.wj.codegen.generatefile.internal.PluginAggregator;

public class Context extends PropertyHolder {
	
	private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;
	private JavaModelGeneratorConfiguration javaModelGeneratorConfiguration;
	private JavaTypeResolverConfiguration javaTypeResolverConfiguration;
	/** 插件聚合器  */
	private PluginAggregator pluginAggregator;
	
	private List<IntrospectedTable> introspectedTables;
	
	public void introspectTables(ProgressCallBack callback, List<String> warnings, 
			Set<String> fullyQualifiedTableNames) {
		introspectedTables = new ArrayList<IntrospectedTable>();
		JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this, warnings);
		
		Connection connection = null;
	}
	
	public void generateFiles(ProgressCallBack callback, List<GeneratedJavaFile> generatedJavaFiles,
			List<GeneratedXmlFile> generatedXmlFiles, List<String> warnings) 
			throws InterruptedException{
		
		pluginAggregator = new PluginAggregator();
		
	}
	
	public JavaClientGeneratorConfiguration getJavaClientGeneratorConfiguration() {
		return javaClientGeneratorConfiguration;
	}

	public void setJavaClientGeneratorConfiguration(JavaClientGeneratorConfiguration javaClientGeneratorConfiguration) {
		this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
	}

	public JavaModelGeneratorConfiguration getJavaModelGeneratorConfiguration() {
		return javaModelGeneratorConfiguration;
	}

	public void setJavaModelGeneratorConfiguration(JavaModelGeneratorConfiguration javaModelGeneratorConfiguration) {
		this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
	}

	public JavaTypeResolverConfiguration getJavaTypeResolverConfiguration() {
		return javaTypeResolverConfiguration;
	}
	
	
}
