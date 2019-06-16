package com.wj.codegen.generatefile.oracle;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.generatefile.internal.ObjectFactory;
import com.wj.codegen.javabean.FullyQualifiedJavaType;

public class RootClassInfo {
	
	private static Map<String, RootClassInfo> rootClassInfoMap;
	
	private String className;
	private List<String> warnings;
	private boolean genericMode = false;
	private PropertyDescriptor[] propertyDescriptors;
	
	static {
		rootClassInfoMap = Collections.synchronizedMap(new HashMap<String,RootClassInfo>());
	}
	
	private RootClassInfo(String className, List<String> warnings) {
		this.className = className;
		this.warnings = warnings;
		if(className == null) {
			return;
		}
		
		FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(className);
		String nameWithOutGenerics = fqjt.getFullyQualifiedName();
		if(!nameWithOutGenerics.equals(className)) {
			genericMode = true;
		}
		
		try {
			Class<?> clazz = ObjectFactory.externalClassForName(nameWithOutGenerics);
			BeanInfo bi = Introspector.getBeanInfo(clazz);
			propertyDescriptors = bi.getPropertyDescriptors();
		}catch(Exception e) {
			propertyDescriptors = null;
			warnings.add("RootClassInfo error");
		}
	}
	
	public boolean containsProperty(IntrospectedColumn introspectedColumn) {
		if(propertyDescriptors == null) {
			return false;
		}
		
		boolean found = false;
		String propertyName = introspectedColumn.getJavaProperty();
		String propertyType = introspectedColumn.getFullQualifiedJavaType().getFullyQualifiedName();
		for(int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
			
			if(propertyDescriptor.getName().equals(propertyName)) {
				String introspectedPropertyType = propertyDescriptor.getPropertyType().getName();
				
				if(genericMode && introspectedPropertyType.equals("java.lang.Object")) {
					
				}else if(!introspectedPropertyType.equals(propertyType)){
					break;
				}
				
				if(propertyDescriptor.getReadMethod() == null) {
					break;
				}
				
				if(propertyDescriptor.getWriteMethod() == null) {
					break;
				}
				found = true;
				break;
			}
			
		}
		return found;
	}
	
	public static RootClassInfo getInstance(String className,List<String> warnings) {
		RootClassInfo classInfo = rootClassInfoMap.get(className);
		if(classInfo == null) {
			classInfo = new RootClassInfo(className, warnings);
			rootClassInfoMap.put(className, classInfo);
		}
		return classInfo;
	}
}
