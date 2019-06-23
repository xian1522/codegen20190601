package com.wj.codegen.generatefile.internal;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.javabean.FullyQualifiedJavaType;
import com.wj.codegen.util.StringUtil;

public class JavaTypeResolverDefaultImpl implements JavaTypeResolver {
	
	protected List<String> warnings;
	protected Properties properties;
	protected boolean forceBigDecimals;
	protected Context context;
	
	protected Map<Integer, JdbcTypeInfomation> typeMap;
	
	public JavaTypeResolverDefaultImpl() {
		properties = new Properties();
		
		typeMap = new HashMap<Integer, JdbcTypeInfomation>();
		
		typeMap.put(Types.CHAR, 
				new JdbcTypeInfomation("CHAR",new FullyQualifiedJavaType(String.class.getName())));
		typeMap.put(Types.DATE, 
				new JdbcTypeInfomation("DATE",new FullyQualifiedJavaType(Date.class.getName())));
		typeMap.put(Types.DECIMAL, 
				new JdbcTypeInfomation("DECIMAL",new FullyQualifiedJavaType(BigDecimal.class.getName())));
		typeMap.put(Types.VARCHAR, 
				new JdbcTypeInfomation("VARCHAR",new FullyQualifiedJavaType(String.class.getName())));
		typeMap.put(Types.TIME, 
				new JdbcTypeInfomation("TIME",new FullyQualifiedJavaType(Date.class.getName())));
		typeMap.put(Types.TIMESTAMP, 
				new JdbcTypeInfomation("TIMESTAMPE",new FullyQualifiedJavaType(Timestamp.class.getName())));
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
		forceBigDecimals = StringUtil.isTrue(properties.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
		FullyQualifiedJavaType answer = null;
		JdbcTypeInfomation jdbcTypeInfomation = typeMap.get(introspectedColumn.getJdbcType());
		if(jdbcTypeInfomation != null) {
			answer = jdbcTypeInfomation.getFullyQualifiedJavaType();
			answer = overrideDefaultType(introspectedColumn, answer);
			
		}
		return answer;
	}
	public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
		String answer = null;
		JdbcTypeInfomation jdbcTypeInfomation = typeMap.get(introspectedColumn.getJdbcType());
		if(jdbcTypeInfomation != null) {
			answer = jdbcTypeInfomation.getJdbcTypeName();
		}
		return answer;
	}
	
	protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column
					, FullyQualifiedJavaType defaultType) {
		FullyQualifiedJavaType answer = defaultType;
		switch(column.getJdbcType()) {
		case Types.BIT:
		case Types.DECIMAL:
		case Types.NUMERIC:
			answer = calculateBigDecimalReplacement(column,defaultType);
			break;
		}
		return answer;
	}
	
	private FullyQualifiedJavaType calculateBigDecimalReplacement(IntrospectedColumn column,
			FullyQualifiedJavaType defaultType) {
		FullyQualifiedJavaType answer;
		
		if(column.getScale() > 0 || column.getLength() > 18 || forceBigDecimals) {
			answer = defaultType;
		}else if(column.getLength() > 9) {
			answer = new FullyQualifiedJavaType(Long.class.getName());
		}else if(column.getLength() > 4) {
			answer = new FullyQualifiedJavaType(Integer.class.getName());
		}else {
			answer = new FullyQualifiedJavaType(Short.class.getName());
		}
		return answer;
	}

	public static class JdbcTypeInfomation{
		private String jdbcTypeName;
		
		private FullyQualifiedJavaType fullyQualifiedJavaType;
		
		public JdbcTypeInfomation(String jdbcTypeName,
				FullyQualifiedJavaType fullyQualifiedJavaType) {
			this.jdbcTypeName = jdbcTypeName;
			this.fullyQualifiedJavaType = fullyQualifiedJavaType;
		}

		public String getJdbcTypeName() {
			return jdbcTypeName;
		}

		public FullyQualifiedJavaType getFullyQualifiedJavaType() {
			return fullyQualifiedJavaType;
		}
		
		
	}
	
}
