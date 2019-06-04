package com.wj.codegen.api;

import com.wj.codegen.config.Context;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.javabean.FullyQualifiedJavaType;

public class IntrospectedColumn {
	protected String actualColumnName;
	
	protected int jdbcType;
	
	protected String jdbcTypeName;
	
	protected boolean nullable;
	
	protected int length;
	
	protected int scale;
	
	protected boolean identity;
	
	protected boolean isSequnceColumn;
	
	protected FullyQualifiedJavaType fullQualifiedJavaType;
	
	protected Context context;
	
	protected String remarks;
	
	protected String defalultValue;
	
	protected String tableAilas;
	
	protected boolean isAutoIncrement;
	protected boolean isGeneratedColumn;
	
	protected boolean isColumnNameDelimited;
	
	protected IntrospectedTable introspectedTable;
	
	public boolean isBLOBColumn() {
		String typeName = this.getJdbcTypeName();
		return "BINARY".equals(typeName) || "BLOB".equals(typeName) || "CLOB".equals(typeName);
	}

	public String getActualColumnName() {
		return actualColumnName;
	}

	public void setActualColumnName(String actualColumnName) {
		this.actualColumnName = actualColumnName;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getJdbcTypeName() {
		return jdbcTypeName;
	}

	public void setJdbcTypeName(String jdbcTypeName) {
		this.jdbcTypeName = jdbcTypeName;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isIdentity() {
		return identity;
	}

	public void setIdentity(boolean identity) {
		this.identity = identity;
	}

	public boolean isSequnceColumn() {
		return isSequnceColumn;
	}

	public void setSequnceColumn(boolean isSequnceColumn) {
		this.isSequnceColumn = isSequnceColumn;
	}

	public FullyQualifiedJavaType getFullQualifiedJavaType() {
		return fullQualifiedJavaType;
	}

	public void setFullQualifiedJavaType(FullyQualifiedJavaType fullQualifiedJavaType) {
		this.fullQualifiedJavaType = fullQualifiedJavaType;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDefalultValue() {
		return defalultValue;
	}

	public void setDefalultValue(String defalultValue) {
		this.defalultValue = defalultValue;
	}

	public String getTableAilas() {
		return tableAilas;
	}

	public void setTableAilas(String tableAilas) {
		this.tableAilas = tableAilas;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public boolean isGeneratedColumn() {
		return isGeneratedColumn;
	}

	public void setGeneratedColumn(boolean isGeneratedColumn) {
		this.isGeneratedColumn = isGeneratedColumn;
	}

	public boolean isColumnNameDelimited() {
		return isColumnNameDelimited;
	}

	public void setColumnNameDelimited(boolean isColumnNameDelimited) {
		this.isColumnNameDelimited = isColumnNameDelimited;
	}

	public IntrospectedTable getIntrospectedTable() {
		return introspectedTable;
	}

	public void setIntrospectedTable(IntrospectedTable introspectedTable) {
		this.introspectedTable = introspectedTable;
	}
	
	
	
}
