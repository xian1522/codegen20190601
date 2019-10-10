package com.wj.codegen.util;

public class StringUtil {
	
	public static boolean stringHasValue(String s) {
		return s != null && s.length() > 0;
	}
	
	public static boolean isTrue(String s) {
		return "true".equalsIgnoreCase(s);
	}
	
	public static boolean stringContainsSpace(String s) {
		return s != null && s.indexOf(' ') != -1;
	}
	
	public static String composeFullyQualifiedTableName(String catalog,
			String schema,String tableName,char separator) {
		StringBuilder sb = new StringBuilder();
		
		if(stringHasValue(catalog)) {
			sb.append(catalog);
			sb.append(separator);
		}
		if(stringHasValue(schema)) {
			sb.append(schema);
			sb.append(separator);
		}else {
			if(sb.length() > 0) {
				sb.append(separator);
			}
		}
		
		sb.append(tableName);
		
		return sb.toString();
	}
	
	public static boolean stringContainSQLWildcard(String s) {
		if(s == null) {
			return false;
		}
		return s.indexOf('_') != -1 || s.indexOf('%') != -1;
	}
	
	/**
	* 首字母大小写
	* @Description
	* @user w.j
	* @date 2019年10月10日 下午5:18:51
	* @throws
	*/
	public static String changeFirstCharacterCase(String str,Boolean capitalize) {
		
		char baseChar = str.charAt(0);
		
		Character updatedChar;
		if(capitalize) {
			updatedChar = Character.toUpperCase(baseChar);		
		}else {
			updatedChar = Character.toLowerCase(baseChar);
		}
		
		if(updatedChar == baseChar) {
			return str;
		}
		
		char[] chars = str.toCharArray();
		chars[0] = updatedChar;
		
		return new String(chars, 0, chars.length);
	}
}
