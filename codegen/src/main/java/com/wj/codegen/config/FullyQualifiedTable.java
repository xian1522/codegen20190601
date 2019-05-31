package com.wj.codegen.config;

import com.wj.codegen.util.StringUtil;

public class FullyQualifiedTable {
	
	private String introspectedCatalog;
	private String introspectedSchema;
	private String introspectedTableName;
	private String runtimeCatalog;
	private String runtimeSchema;
	private String runtimeTableName;
	
	private String domainObjectName;
	private String domainObjectSubPackage;
	private String alias;
	private boolean ignoreQualifiersAtRuntime;
	private String beginningDelimiter;
	private String endingDelimiter;
	
	protected String getSubPackageForClientOrSqlMap(boolean isSubPackageEnabled) {
		StringBuilder sb = new StringBuilder();
		if(!ignoreQualifiersAtRuntime && isSubPackageEnabled) {
			if(StringUtil.stringHasValue(runtimeCatalog)) {
				sb.append('.');
				sb.append(runtimeCatalog.toLowerCase());
			}else if(StringUtil.stringHasValue(introspectedCatalog)) {
				sb.append('.');
				sb.append(introspectedCatalog.toLowerCase());
			}
		}
		
		if(StringUtil.stringHasValue(runtimeSchema)) {
			sb.append('.');
			sb.append(runtimeSchema.toLowerCase());
		}else if(StringUtil.stringHasValue(introspectedSchema)) {
			sb.append('.');
			sb.append(introspectedSchema.toLowerCase());
		}
		return sb.toString();
	}
	
	public String getIntrospectedCatalog() {
		return introspectedCatalog;
	}
	public String getIntrospectedSchema() {
		return introspectedSchema;
	}
	public String getIntrospectedTableName() {
		return introspectedTableName;
	}
	public String getRuntimeCatalog() {
		return runtimeCatalog;
	}
	public String getRuntimeSchema() {
		return runtimeSchema;
	}
	public String getRuntimeTableName() {
		return runtimeTableName;
	}
	public String getDomainObjectName() {
		return domainObjectName;
	}
	public String getDomainObjectSubPackage() {
		return domainObjectSubPackage;
	}
	public String getAlias() {
		return alias;
	}
	public boolean isIgnoreQualifiersAtRuntime() {
		return ignoreQualifiersAtRuntime;
	}
	public String getBeginningDelimiter() {
		return beginningDelimiter;
	}
	public String getEndingDelimiter() {
		return endingDelimiter;
	}
	
	
	
}
