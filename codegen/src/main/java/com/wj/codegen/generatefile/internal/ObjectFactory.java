package com.wj.codegen.generatefile.internal;

import java.util.ArrayList;
import java.util.List;

import com.wj.codegen.api.ConnectionFactory;
import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.ConnectionFactoryConfiguration;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.FullyQualifiedTable;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.JavaTypeResolverConfiguration;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.generatefile.DefaultJavaFormatter;
import com.wj.codegen.generatefile.JavaFormatter;
import com.wj.codegen.generatefile.XmlDefaultFormatter;
import com.wj.codegen.generatefile.XmlFormatter;
import com.wj.codegen.generatefile.oracle.IntrospectedTableOracleImpl;
import com.wj.codegen.util.StringUtil;

public class ObjectFactory {
	
	private static List<ClassLoader> externalClassLoaders;
	
	static {
		externalClassLoaders = new ArrayList<ClassLoader>();
	}
	
	public static void reset() {
		externalClassLoaders.clear();
	}
	
	public static JavaTypeResolver createJavaTypeResolver(Context context, List<String> warnings) {
		
		JavaTypeResolverConfiguration config = context.getJavaTypeResolverConfiguration();
		String type;
		
		if(config != null && config.getConfigurationType() != null) {
			type = config.getConfigurationType();
			if("DEFAULT".equalsIgnoreCase(type)) {
				type = JavaTypeResolverDefaultImpl.class.getName();
			}
		}else {
			type = JavaTypeResolverDefaultImpl.class.getName();
		}
		
		JavaTypeResolver answer = (JavaTypeResolver)createInternalObject(type);
		answer.setWarnings(warnings);
		
		if(config != null) {
			answer.addConfigurationProperties(config.getProperties());
		}
		answer.setContext(context);
		
		return answer;
		
	}
	
	public static Object createInternalObject(String type) {
		Object answer;
		
		try {
			Class<?> clazz = internalClassForName(type);
			answer = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("",e);
		}
		
		return answer;
	}
	
	public static Class<?> internalClassForName(String type) throws ClassNotFoundException{
		Class<?> clazz = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			clazz = Class.forName(type, true, cl);
		}catch(Exception e) {
			
		}
		
		if(clazz == null) {
			clazz = Class.forName(type, true, ObjectFactory.class.getClassLoader());
		}
		return clazz;
	}
	
	/**
	 * 为什么需要提供额外的类加载器？
	 * @param type
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> externalClassForName(String type) throws ClassNotFoundException{
		Class<?> clazz;
		for(ClassLoader classLoader : externalClassLoaders) {
			try {
				clazz = Class.forName(type, true, classLoader);
				return clazz;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return internalClassForName(type);
	}
	
	public static ConnectionFactory createConnectionFactory(Context context) {
		ConnectionFactoryConfiguration config = context.getConnectionFactoryConfigration();
		ConnectionFactory answer;
		
		String type;
		if(config == null || config.getConfigurationType() == null) {
			type = JDBCConnectionFactory.class.getName();
		}else {
			type = config.getConfigurationType();
		}
		
		answer = (ConnectionFactory) createInternalObject(type);
		
		if((config != null)) {
			answer.addConfigurationProperties(config.getProperties());
		}
		return answer;
	}
	
	public static IntrospectedColumn createIntrospectedColumn(Context context) {
		String type = context.getIntrospectedColumnImpl();
		if(!StringUtil.stringHasValue(type)) {
			type = IntrospectedColumn.class.getName();
		}
		
		IntrospectedColumn answer = (IntrospectedColumn)createInternalObject(type);
		answer.setContext(context);
		
		return answer;
	}
	
	public static IntrospectedTable createIntrospectedTable(TableConfiguration tc,
			FullyQualifiedTable table,Context context) {
		IntrospectedTable answer = createIntrospectedTableForValidation(context);
		answer.setFullyQualifiedTable(table);
		answer.setTableConfiguration(tc);
		
		return answer;
	}
	
	public static IntrospectedTable createIntrospectedTableForValidation(Context context) {
		String type = context.getTargetRuntime();
		if(!StringUtil.stringHasValue(type)) {
			type = IntrospectedTableOracleImpl.class.getName();
		}
		IntrospectedTable answer = (IntrospectedTable) createInternalObject(type);
		answer.setContext(context);
		
		return answer;
	}

	public static JavaFormatter createJavaFormatter(Context context) {
		String type = context.getProperty(PropertyRegistry.CONTEXT_JAVA_FORMATTER);
		if(!StringUtil.stringHasValue(type)) {
			type = DefaultJavaFormatter.class.getName(); 
		}
		JavaFormatter answer = (JavaFormatter)createInternalObject(type);
		answer.setContext(context);
		return answer;
	}
	
	public static XmlFormatter createXmlFormatter(Context context) {
		String type = context.getProperty(PropertyRegistry.CONTEXT_XML_FORMATTER);
		if(!StringUtil.stringHasValue(type)) {
			type = XmlDefaultFormatter.class.getName();
		}
		XmlFormatter answer = (XmlFormatter) createInternalObject(type);
		
		answer.setContext(context);
		
		return answer;
	}
}
