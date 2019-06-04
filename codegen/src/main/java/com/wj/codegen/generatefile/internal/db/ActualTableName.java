package com.wj.codegen.generatefile.internal.db;

import com.wj.codegen.util.StringUtil;

public class ActualTableName {
	private String tableName;
	private String catalog;
	private String schema;
	private String fullName;
	
	public ActualTableName(String catalog,String schema,String tableName) {
		this.catalog = catalog;
		this.schema = schema;
		this.tableName = tableName;
		this.fullName = StringUtil.composeFullyQualifiedTableName(catalog, schema, tableName, '.');
	}
	
	public String getTableName() {
		return tableName;
	}
	public String getSchema() {
		return schema;
	}

	public String getCatalog() {
		return catalog;
	}
	
	@Override
	public String toString() {
		return fullName;
	}

}
