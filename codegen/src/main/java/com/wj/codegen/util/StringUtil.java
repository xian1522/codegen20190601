package com.wj.codegen.util;

public class StringUtil {
	
	public static boolean stringHasValue(String s) {
		return s != null && s.length() > 0;
	}
	
	public static boolean isTrue(String s) {
		return "true".equalsIgnoreCase(s);
	}
}
