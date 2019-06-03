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
	
	public FullyQualifiedTable(String introspectedCatalog,String introspectedSchema,
			String introspectedTableName,String domainObjectName,String alias,
			boolean ignoreQualifiersAtRuntime,String runtimeCatalog,
			String runtimeSchema,String runtimeTableName,
			boolean delimitIdentifiers,Context context) {
		this.introspectedCatalog = introspectedCatalog;
		this.introspectedSchema = introspectedSchema;
		this.introspectedTableName = introspectedTableName;
		this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
		this.runtimeCatalog = runtimeCatalog;
		this.runtimeSchema = runtimeSchema;
		this.runtimeTableName =runtimeTableName;
		
		if(StringUtil.stringHasValue(domainObjectName)) {
			int index = domainObjectName.lastIndexOf('.');
			if(index == -1) {
				this.domainObjectName = domainObjectName;
			}else {
				this.domainObjectName = domainObjectName.substring(index+1);
				this.domainObjectSubPackage = domainObjectName.substring(0, index);
			}
		}
		
		if(alias == null) {
			this.alias = null;
		}else {
			this.alias = alias.trim();
		}
		
		beginningDelimiter = delimitIdentifiers ? context.getBeginningDelimiter() : "";
		endingDelimiter = delimitIdentifiers ? context.getEndingDelimiter() : "";
	}
	
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
