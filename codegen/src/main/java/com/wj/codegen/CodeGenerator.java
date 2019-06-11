package com.wj.codegen;

import com.wj.codegen.config.Context;
import com.wj.codegen.config.GeneratedKey;
import com.wj.codegen.config.JDBCConnectionConfiguration;
import com.wj.codegen.config.ModelType;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.util.StringUtil;

public class CodeGenerator {
	
	private static final String JDBC_URL = "jdbc:oracle:thin:127.0.0.1:1521:orcl";
	private static final String JDBC_USERNAME = "ticmsc";
	private static final String JDBC_PASSWORD = "joyin123";
	private static final String JDBC_DIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	
	public static void genModelAndMapper(String tableName,String modelName) {
		Context context = new Context(ModelType.HIERARCHICAL);
		context.setId("Potato");
		context.setTargetRuntime("Hibernate");
		
		JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
		jdbcConfig.setConnectionURL(JDBC_URL);
		jdbcConfig.setUserId(JDBC_USERNAME);
		jdbcConfig.setPassword(JDBC_PASSWORD);
		jdbcConfig.setDriverClass(JDBC_DIVER_CLASS_NAME);
		context.setJdbcConnectionConfiguration(jdbcConfig);
		
		TableConfiguration tableConfiguration = new TableConfiguration(context);
		tableConfiguration.setTableName(tableName);
		if(StringUtil.stringHasValue(modelName)) {
			tableConfiguration.setDomainObjectName(modelName);
		}
		tableConfiguration.setGeneratedKey(new GeneratedKey("id","Mysql",true,null));
		
	}
}
