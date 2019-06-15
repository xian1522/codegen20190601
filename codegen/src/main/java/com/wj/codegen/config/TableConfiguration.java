package com.wj.codegen.config;

public class TableConfiguration extends PropertyHolder {
	
	private String catalog;
	private String schema;
	private String tableName;
	private String alias;
	
	private String domainObjectName;
	
	private GeneratedKey generatedKey;
	
	/** 分隔标识符 */
	private boolean delimitIdentifiers;
	
	private boolean wildcardEscapingEnabled;
	
	private ModelType modelType;
	
	/**是否生成INSERT*/
	private boolean insertStatementEnabled;
	private boolean selectByExampleStatementEnabled;
	private boolean updateByPrimaryKeyStatementEnabled;
	private boolean deleteByPrimaryKeyStatementEnabled;
	
	public TableConfiguration(Context context) {
		this.modelType = context.getDefaultModelType();
		
		selectByExampleStatementEnabled = true;
	}
	
	
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public GeneratedKey getGeneratedKey() {
		return generatedKey;
	}

	public void setGeneratedKey(GeneratedKey generatedKey) {
		this.generatedKey = generatedKey;
	}

	public String getDomainObjectName() {
		return domainObjectName;
	}

	public void setDomainObjectName(String domainObjectName) {
		this.domainObjectName = domainObjectName;
	}

	public ModelType getModelType() {
		return modelType;
	}

	public boolean isInsertStatementEnabled() {
		return insertStatementEnabled;
	}

	public void setInsertStatementEnabled(boolean insertStatementEnabled) {
		this.insertStatementEnabled = insertStatementEnabled;
	}

	public boolean isSelectByExampleStatementEnabled() {
		return selectByExampleStatementEnabled;
	}

	public void setSelectByExampleStatementEnabled(boolean selectByExampleStatementEnabled) {
		this.selectByExampleStatementEnabled = selectByExampleStatementEnabled;
	}

	public boolean isUpdateByPrimaryKeyStatementEnabled() {
		return updateByPrimaryKeyStatementEnabled;
	}

	public void setUpdateByPrimaryKeyStatementEnabled(boolean updateByPrimaryKeyStatementEnabled) {
		this.updateByPrimaryKeyStatementEnabled = updateByPrimaryKeyStatementEnabled;
	}

	public boolean isDeleteByPrimaryKeyStatementEnabled() {
		return deleteByPrimaryKeyStatementEnabled;
	}

	public void setDeleteByPrimaryKeyStatementEnabled(boolean deleteByPrimaryKeyStatementEnabled) {
		this.deleteByPrimaryKeyStatementEnabled = deleteByPrimaryKeyStatementEnabled;
	}
	
	
}
