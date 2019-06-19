package com.wj.codegen.util;

import java.util.Properties;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.javabean.Field;
import com.wj.codegen.javabean.FullyQualifiedJavaType;
import com.wj.codegen.javabean.JavaVisibility;
import com.wj.codegen.javabean.Method;
import com.wj.codegen.javabean.Parameter;

public class JavaBeansUtil {
	
	public static Field getJavaBeanField(IntrospectedColumn introspectedColumn,Context context,
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType fqjt = introspectedColumn.getFullQualifiedJavaType();
		String property = introspectedColumn.getJavaProperty();
		
		Field field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setType(fqjt);
		field.setName(property);
		
		return field;
	}

	public static Method getJavaBeansGetter(IntrospectedColumn introspectedColumn, Context context,
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType fqjt = introspectedColumn.getFullQualifiedJavaType();
		String property = introspectedColumn.getJavaProperty();
		
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(fqjt);
		method.setName(getGetterMethodName(property,fqjt));
		
		StringBuilder sb = new StringBuilder();
		sb.append(" return ");
		sb.append(property);
		sb.append(';');
		method.addBodyLine(sb.toString());
		
		return method;
	}

	private static String getGetterMethodName(String property, FullyQualifiedJavaType fqjt) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(property);
		if(Character.isLowerCase(sb.charAt(0))) {
			if(sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			}
		}
		
		if(fqjt.equals(FullyQualifiedJavaType.getBooleanPrimitiveInstance())) {
			sb.insert(0, "is");
		}else {
			sb.insert(0, "get");
		}
		return sb.toString();
	}

	public static Method getJavaBeanSetter(IntrospectedColumn introspectedColumn, Context context,
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType fqjt = introspectedColumn.getFullQualifiedJavaType();
		String property =introspectedColumn.getJavaProperty();
		
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(getSetterMethodName(property));
		method.addParameter(new Parameter(fqjt,property));
		
		StringBuilder sb = new StringBuilder();
		if(introspectedColumn.isStringColumn() || isTrimStringsEnabled(introspectedColumn)) {
			sb.append("this.");
			sb.append(property);
			sb.append(" = ");
			sb.append(property);
			sb.append(" == null ? null : ");
			sb.append(property);
			sb.append(".trim();");
			method.addBodyLine(sb.toString());
		}else {
			sb.append("this.");
			sb.append(property);
			sb.append(" = ");
			sb.append(property);
			sb.append(";");
			method.addBodyLine(sb.toString());
		}
		return method;
		
	}
	
	public static String getSetterMethodName(String property) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(property);
		if(Character.isLowerCase(sb.charAt(0))) {
			if(sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			}
		}
		
		sb.insert(0,"set");
		
		return sb.toString();
	}
	
	private static boolean isTrimStringsEnabled(IntrospectedColumn column) {
		String trimSpaces = column.getProperties().getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS);
		if(trimSpaces != null) {
			return StringUtil.isTrue(trimSpaces);
		}
		return isTrimStringsEnabled(column.getIntrospectedTable());
	}
	
	private static boolean isTrimStringsEnabled(IntrospectedTable table) {
		TableConfiguration tableConfiguration = table.getTableConfiguration();
		String trimSpaces = tableConfiguration.getProperties().getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS);
		if(trimSpaces != null) {
			return StringUtil.isTrue(trimSpaces);
		}
		return isTrimStringsEnabled(table.getContext());
	}
	
	private static boolean isTrimStringsEnabled(Context context) {
		Properties properties = context.getJavaModelGeneratorConfiguration().getProperties();
		boolean rc = StringUtil.isTrue(properties.getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS));
		return rc;
	}
	
	public static String getCamelCaseString(String inputString,
            boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
            case '_':
            case '-':
            case '@':
            case '$':
            case '#':
            case ' ':
            case '/':
            case '&':
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
                break;

            default:
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
                break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }
}
