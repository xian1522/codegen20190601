package com.wj.codegen;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wj.codegen.config.Configuration;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.GeneratedKey;
import com.wj.codegen.config.JDBCConnectionConfiguration;
import com.wj.codegen.config.JavaClientGeneratorConfiguration;
import com.wj.codegen.config.JavaModelGeneratorConfiguration;
import com.wj.codegen.config.ModelType;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.config.SqlMapGeneratorConfiguration;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.generatefile.callback.DefaultShellCallback;
import com.wj.codegen.util.StringUtil;

import freemarker.template.TemplateExceptionHandler;

public class CodeGenerator {
	
	private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.70.121:1521:orcl";
	private static final String JDBC_USERNAME = "gxcs0917";
	private static final String JDBC_PASSWORD = "gxcs0917";
	private static final String JDBC_DIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	
//	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jeesite";
//    private static final String JDBC_USERNAME = "root";
//    private static final String JDBC_PASSWORD = "root";
//    private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	private static final String PROJECT_PATH = System.getProperty("user.dir");
	private static final String JAVA_PATH = "/src/main/java"; //java文件路径
	 
	private static final String BASE_PACKAGE = "com.wj.codegen";
	private static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
	private static final String RESOURCE_PATH = "/src/main/resources";
	
	private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + RESOURCE_PATH;
	
	private static final String MODEL_PACKAGE = BASE_PACKAGE + ".dep.allowance.model";
	
	
	
	public static void main(String[] args) {
		genCode("DEP_ALLOWANCE_DETAIL");
		//genController("repo_deal");
	}
	
	public static void genCode(String...tableNames) {
		for(String tableName : tableNames) {
			genCodeByCustomModelName(tableName,null);
		}
	}
	
	public static void genCodeByCustomModelName(String tableName,String modelName) {
		genModelAndMapper(tableName,modelName);
		//genController(modelName);
	}
	
	private static void genController(String modelName) {
		try {
			freemarker.template.Configuration configuration = 
					new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
			configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
			configuration.setDefaultEncoding("UTF-8");
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			
			Map<String,Object> data = new HashMap<String,Object>();
			
			data.put("basePackage", "com.joyin.ticm.sl.countdraw");
			data.put("modelNameUpperCamel", "SlCountDraw");
			data.put("modelNameFirstLower", "slCountDraw");
			
			File genFile = new File(PROJECT_PATH+JAVA_PATH+packageConvertPath(BASE_PACKAGE)+"SlCountDrawAction.java");
			if(!genFile.getParentFile().exists()) {
				genFile.getParentFile().mkdirs();
			}
			
			configuration.getTemplate("controller.ftl").process(data, new FileWriter(genFile));
			System.out.println("controller 生成成功");
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		javaModelGeneratorConfig.setTargetPackage(MODEL_PACKAGE);
		context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfig);
		
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH+JAVA_PATH);
		javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
		//javaClientGenerator配置
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
		context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		
		SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCE_PATH);
		sqlMapGeneratorConfiguration.setTargetPackage("mapper");
		context.setSqlMapGeneratroConfiguration(sqlMapGeneratorConfiguration);
		
		TableConfiguration tableConfiguration = new TableConfiguration(context);
		tableConfiguration.setTableName(tableName);
		tableConfiguration.setSchema("GXCS0917");
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
	
	private static String packageConvertPath(String packageName) {
		return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
	}
}
