package com.wj.codegen.config;

public class GeneratedKey {
	private String column;
	
	private boolean isIdentity;
	
	private String type;
	
	private String runtimeSqlStatement;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public boolean isIdentity() {
		return isIdentity;
	}

	public void setIdentity(boolean isIdentity) {
		this.isIdentity = isIdentity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRuntimeSqlStatement() {
		return runtimeSqlStatement;
	}
	
	public boolean isJdbcStandard() {
		return "JDBC".equals(runtimeSqlStatement);
	}
	
}
