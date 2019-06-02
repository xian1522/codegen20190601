package com.wj.codegen.config;

public class TableConfiguration extends PropertyHolder {
	
	private String catalog;
	private String schema;
	private String tableName;
	
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
	
	
}
