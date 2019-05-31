package com.wj.codegen.generatefile.internal;

import java.util.List;

import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.JavaTypeResolverConfiguration;

public class ObjectFactory {
	
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
		clazz = Class.forName(type, true, cl);
		
		if(clazz == null) {
			clazz = Class.forName(type, true, ObjectFactory.class.getClassLoader());
		}
		return clazz;
	}
	
}
