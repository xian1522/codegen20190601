package com.wj.codegen;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import com.wj.codegen.util.ContextLoader;
import com.wj.codegen.util.StringUtil;

import freemarker.template.TemplateExceptionHandler;

public class CodeGenerator {
	
	private String JDBC_URL = null;
	private String JDBC_USERNAME = null;
	private String JDBC_PASSWORD = null;
	private String JDBC_DIVER_CLASS_NAME = null;
	
	private final String PROJECT_PATH_BUSINESS = System.getProperty("user.dir")+"_Business";
	private final String PROJECT_PATH = System.getProperty("user.dir");
	
	private String JAVA_PATH = null; //java文件路径
	 
	private String BASE_PACKAGE = null;
	private String DAO_PACKAGE = null;
	private String SERVICE_PACKAGE = null;
	private String RESOURCE_PATH = null;
	private String TEMPLATE_RESOURCE_PATH = null;
	
	private final String TEMPLATE_FILE_PATH = System.getProperty("user.dir") + TEMPLATE_RESOURCE_PATH;
	
	private String MAPPER_PATH = null;
	private String MODEL_PACKAGE = null;
	
	private static freemarker.template.Configuration config;
	
	private ContextLoader contextLoader = null;
	
	
	public CodeGenerator() {
		//加载properties配置文件
		if(contextLoader == null) {
			contextLoader = ContextLoader.getInstance();
		}
		Properties properties = contextLoader.getProperties();
		JDBC_URL = properties.getProperty("jdbc.url");
		JDBC_USERNAME = properties.getProperty("jdbc.username");
		JDBC_PASSWORD = properties.getProperty("jdbc.password");
		JDBC_DIVER_CLASS_NAME = properties.getProperty("jdbc.driverClassName");
		
		JAVA_PATH = properties.getProperty("java.path");
		
		BASE_PACKAGE = properties.getProperty("base.package");
		DAO_PACKAGE = properties.getProperty("dao.package");
		SERVICE_PACKAGE = properties.getProperty("service.package");
		RESOURCE_PATH = properties.getProperty("resource.path");
		TEMPLATE_RESOURCE_PATH = properties.getProperty("template.resource.path");
		MODEL_PACKAGE = properties.getProperty("model.package");
		MAPPER_PATH = properties.getProperty("mapper.path");
		
	}
	
	public static void main(String[] args) {
		CodeGenerator codeGenerator = new CodeGenerator();
		codeGenerator.genCode("SL_DEAL");
	}
	
	public void genCode(String...tableNames) {
		for(String tableName : tableNames) {
			genCodeByCustomModelName(tableName,null);
		}
	}
	
	public void genCodeByCustomModelName(String tableName,String modelName) {
		genModelAndMapper(tableName,modelName);
	//	genController(modelName);
	//	genService("SlDeal");
	//	genDao("SlDeal");
	}
	
	@SuppressWarnings("unused")
	private void genController(String modelName) {
		try {
			if(config == null) {
				config = getConfiguration();
			}
			Map<String,Object> data = new HashMap<String,Object>();
			
			data.put("basePackage", "com.joyin.ticm.sl.countdraw");
			data.put("modelNameUpperCamel", "SlCountDraw");
			data.put("modelNameFirstLower", "slCountDraw");
			
			File genFile = new File(PROJECT_PATH_BUSINESS+JAVA_PATH+packageConvertPath(BASE_PACKAGE)+"SlCountDrawAction.java");
			if(!genFile.getParentFile().exists()) {
				genFile.getParentFile().mkdirs();
			}
			config.getTemplate("controller.ftl").process(data, new FileWriter(genFile));
			System.out.println("controller 生成成功");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void genService(String modelName) {
		try {
			if(config == null) {
				config = getConfiguration();
			}
			Map<String,Object> data = new HashMap<String,Object>();
			
			data.put("basePackage", "com.joyin.ticm.sl.deal");
			data.put("upperCamelModel", modelName);
			data.put("noteName", "债券借贷");
			
			String firstLowerModel = StringUtil.changeFirstCharacterCase(modelName, false);
			data.put("firstLowerModel", firstLowerModel);
			
			File genFile = new File(PROJECT_PATH_BUSINESS+JAVA_PATH+packageConvertPath(SERVICE_PACKAGE)+"impl/"+ modelName + "ServiceImplTest.java");
			if(!genFile.getParentFile().exists()) {
				genFile.getParentFile().mkdirs();
			}
			config.getTemplate("serviceImpl.ftl").process(data, new FileWriter(genFile));
			System.out.println("serviceImpl 生成成功");
			
			File genFileImpl = new File(PROJECT_PATH_BUSINESS+JAVA_PATH+packageConvertPath(SERVICE_PACKAGE) + modelName + "ServiceTest.java");
			if(!genFileImpl.getParentFile().exists()) {
				genFileImpl.getParentFile().mkdirs();
			}
			config.getTemplate("service.ftl").process(data, new FileWriter(genFileImpl));
			System.out.println("service 生成成功");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void genDao(String modelName) {
		try {
			if(config == null) {
				config = getConfiguration();
			}
			Map<String,Object> data = new HashMap<String,Object>();
			
			data.put("basePackage", "com.joyin.ticm.sl.deal");
			data.put("upperCamelModel", modelName);
			
			String firstLowerModel = StringUtil.changeFirstCharacterCase(modelName, false);
			data.put("firstLowerModel", firstLowerModel);
			
			File genFile = new File(PROJECT_PATH_BUSINESS+JAVA_PATH+packageConvertPath(DAO_PACKAGE)+"impl/SlDealDaoImplTest.java");
			if(!genFile.getParentFile().exists()) {
				genFile.getParentFile().mkdirs();
			}
			config.getTemplate("daoImpl.ftl").process(data, new FileWriter(genFile));
			System.out.println("daoImpl 生成成功");
			
			File genFileImpl = new File(PROJECT_PATH_BUSINESS+JAVA_PATH+packageConvertPath(DAO_PACKAGE)+"SlDealDaoTest.java");
			if(!genFileImpl.getParentFile().exists()) {
				genFileImpl.getParentFile().mkdirs();
			}
			config.getTemplate("dao.ftl").process(data, new FileWriter(genFileImpl));
			System.out.println("dao 生成成功");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void genModelAndMapper(String tableName,String modelName) {
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
		javaModelGeneratorConfig.setTargetProject(PROJECT_PATH_BUSINESS+JAVA_PATH);
		javaModelGeneratorConfig.setTargetPackage(MODEL_PACKAGE);
		context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfig);
		
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH_BUSINESS+JAVA_PATH);
		javaClientGeneratorConfiguration.setTargetPackage(DAO_PACKAGE);
		//javaClientGenerator配置
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
		context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		
		SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCE_PATH);
		sqlMapGeneratorConfiguration.setTargetPackage(MAPPER_PATH);
		context.setSqlMapGeneratroConfiguration(sqlMapGeneratorConfiguration);
		
		TableConfiguration tableConfiguration = new TableConfiguration(context);
		tableConfiguration.setTableName(tableName);
		tableConfiguration.setSchema(JDBC_USERNAME);
		if(StringUtil.stringHasValue(modelName)) {
			tableConfiguration.setDomainObjectName(modelName);
		}
		tableConfiguration.setGeneratedKey(new GeneratedKey("id","Mysql",true,null));
		tableConfiguration.addProperty(PropertyRegistry.ANY_ROOT_CLASS, "com.joyin.ticm.bean.DataForm");
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
	
	public freemarker.template.Configuration getConfiguration() {
		
		freemarker.template.Configuration configuration = 
				new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
		try {
			configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
			configuration.setDefaultEncoding("UTF-8");
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return configuration;
	}
	
	private String packageConvertPath(String packageName) {
		return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
	}
}
