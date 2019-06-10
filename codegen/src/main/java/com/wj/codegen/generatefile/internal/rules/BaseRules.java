package com.wj.codegen.generatefile.internal.rules;

import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.util.StringUtil;

public abstract class BaseRules implements Rules {
	
	protected TableConfiguration tableConfiguration;
	protected IntrospectedTable introspectedTable;
	protected final boolean isModelOnly;
	
	public BaseRules(IntrospectedTable introspectedTable) {
		this.introspectedTable = introspectedTable;
		this.tableConfiguration = introspectedTable.getTableConfiguration();
		String modelOnly = tableConfiguration.getProperty(PropertyRegistry.TABLE_MODEL_ONLY);
		this.isModelOnly = StringUtil.isTrue(modelOnly);
	}

	public boolean generateInsert() {
		if(isModelOnly) {
			return false;
		}
		return tableConfiguration.isInsertStatementEnabled();
	}

	public IntrospectedTable getIntrospectedTable() {
		return introspectedTable;
	}
	
	public boolean generateExampleClass() {
		if(introspectedTable.getContext().getJavaClientGeneratorConfiguration() == null) {
			return false;
		}
		
		if(isModelOnly) {
			return false;
		}
		
		boolean rc = tableConfiguration.isDeleteByPrimaryKeyStatementEnabled()
				|| tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
				|| tableConfiguration.isSelectByExampleStatementEnabled();
		return rc;
	}

}
