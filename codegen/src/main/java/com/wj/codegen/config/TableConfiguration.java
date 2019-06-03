package com.wj.codegen.config;

public class TableConfiguration extends PropertyHolder {
	
	private String catalog;
	private String schema;
	private String tableName;
	
	/** 分隔标识符 */
	private boolean delimitIdentifiers;
	
	private boolean wildcardEscapingEnabled;
	
	public boolean areAnyStatementsEnabled() {
		return true;
	}
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isDelimitIdentifiers() {
		return delimitIdentifiers;
	}

	public void setDelimitIdentifiers(boolean delimitIdentifiers) {
		this.delimitIdentifiers = delimitIdentifiers;
	}

	public boolean isWildcardEscapingEnabled() {
		return wildcardEscapingEnabled;
	}

	public void setWildcardEscapingEnabled(boolean wildcardEscapingEnabled) {
		this.wildcardEscapingEnabled = wildcardEscapingEnabled;
	}
	
	
}
