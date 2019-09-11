package com.wj.codegen;

import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.wj.codegen.config.Configuration;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.GeneratedKey;
import com.wj.codegen.config.JDBCConnectionConfiguration;
import com.wj.codegen.config.JavaClientGeneratorConfiguration;
import com.wj.codegen.config.JavaModelGeneratorConfiguration;
import com.wj.codegen.config.ModelType;
import com.wj.codegen.config.ProjectConstant;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.generatefile.callback.DefaultShellCallback;
import com.wj.codegen.util.StringUtil;

public class CodeGenerator {
	
	private static final String JDBC_URL = "jdbc:oracle:thin:127.0.0.1:1521:orcl";
	private static final String JDBC_USERNAME = "ticmsc";
	private static final String JDBC_PASSWORD = "joyin123";
	private static final String JDBC_DIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	
//	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jeesite";
//    private static final String JDBC_USERNAME = "root";
//    private static final String JDBC_PASSWORD = "root";
//    private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	 private static final String PROJECT_PATH = System.getProperty("user.dir");
	 private static final String JAVA_PATH = "/src/main/java"; //java文件路径
	 
	 private static final String BASE_PACKAGE = "com.wj.codegen";
	 private static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
	
	public static void main(String[] args) {
		genCode("repo_deal");
	}
	
	public static void genCode(String...tableNames) {
		for(String tableName : tableNames) {
			genCodeByCustomModelName(tableName,null);
		}
	}
	
	public static void genCodeByCustomModelName(String tableName,String modelName) {
		genModelAndMapper(tableName,modelName);
	}
	
	public static void genModelAndMapper(String tableName,String modelName) {
		Context context = new Context(ModelType.HIERARCHICAL);
		context.setId("Potato");
		/** 不指定 默认为IntrospectedTableOracleImpl*/
		//context.setTargetRuntime("Hibernate");
		
		JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
		jdbcConfig.setConnectionURL(JDBC_URL);
		jdbcConfig.setUserId(JDBC_USERNAME);
		jdbcConfig.setPassword(JDBC_PASSWORD);
		jdbcConfig.setDriverClass(JDBC_DIVER_CLASS_NAME);
		context.setJdbcConnectionConfiguration(jdbcConfig);
		
		JavaModelGeneratorConfiguration javaModelGeneratorConfig = new JavaModelGeneratorConfiguration();
		javaModelGeneratorConfig.setTargetProject(PROJECT_PATH+JAVA_PATH);
		javaModelGeneratorConfig.setTargetPackage(ProjectConstant.MODEL_PACKAGE);
		context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfig);
		
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH+JAVA_PATH);
		javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
		//javaClientGenerator配置
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
		context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		
		TableConfiguration tableConfiguration = new TableConfiguration(context);
		tableConfiguration.setTableName(tableName);
		tableConfiguration.setSchema("TICMSC");
		if(StringUtil.stringHasValue(modelName)) {
			tableConfiguration.setDomainObjectName(modelName);
		}
		tableConfiguration.setGeneratedKey(new GeneratedKey("id","Mysql",true,null));
		tableConfiguration.addProperty(PropertyRegistry.ANY_ROOT_CLASS, "com.wj.codegen.exception.ShellException");
		context.addTableConfiguration(tableConfiguration);
		
		List<String> warnings;
		HibernateCodeGenerator generator;
		try {
			Configuration config = new Configuration();
			config.addContext(context);
			
			boolean overwrite = true;
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			warnings = new ArrayList<String>();
			generator = new HibernateCodeGenerator(config,callback,warnings);
			generator.generate(null);
			
		}catch(Exception e) {
			throw new RuntimeException("生成Model失败",e);
		}
		
		System.out.println("model 生成成功");
		
	}
}
