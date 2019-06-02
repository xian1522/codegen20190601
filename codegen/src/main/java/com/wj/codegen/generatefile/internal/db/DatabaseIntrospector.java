package com.wj.codegen.generatefile.internal.db;

import java.sql.DatabaseMetaData;
import java.util.List;

import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.TableConfiguration;

public class DatabaseIntrospector {
	
	private DatabaseMetaData databaseMetaData;
	
	private JavaTypeResolver javaTypeResolver;
	
	private List<String> warnings;
	
	private Context context;
	
	public List<IntrospectedTable> introspectTables(TableConfiguration tc){
		
		
		
		return null;
	}
	
	public DatabaseIntrospector(Context context, DatabaseMetaData databaseMetaData,
			JavaTypeResolver javaTypeResolver, List<String> warnings) {
		this.context = context;
		this.databaseMetaData = databaseMetaData;
		this.javaTypeResolver = javaTypeResolver;
		this.warnings = warnings;
	}
	
}
