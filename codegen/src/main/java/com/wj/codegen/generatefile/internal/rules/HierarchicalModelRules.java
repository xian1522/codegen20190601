package com.wj.codegen.generatefile.internal.rules;

import com.wj.codegen.config.IntrospectedTable;

public class HierarchicalModelRules extends BaseRules {

	public HierarchicalModelRules(IntrospectedTable introspectedTable) {
		super(introspectedTable);
	}

	public boolean generatePrimaryKeyClass() {
		return introspectedTable.hasPrimaryKeyColumns();
	}

}
