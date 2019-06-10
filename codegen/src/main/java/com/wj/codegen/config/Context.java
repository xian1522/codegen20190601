package com.wj.codegen.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.wj.codegen.api.ConnectionFactory;
import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.generatefile.GeneratedJavaFile;
import com.wj.codegen.generatefile.GeneratedXmlFile;
import com.wj.codegen.generatefile.JavaFormatter;
import com.wj.codegen.generatefile.callback.ProgressCallBack;
import com.wj.codegen.generatefile.internal.JDBCConnectionFactory;
import com.wj.codegen.generatefile.internal.ObjectFactory;
import com.wj.codegen.generatefile.internal.PluginAggregator;
import com.wj.codegen.generatefile.internal.db.DatabaseIntrospector;
import com.wj.codegen.util.StringUtil;

public class Context extends PropertyHolder {
	
	private ModelType defaultModelType;
	
	private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;
	private JavaModelGeneratorConfiguration javaModelGeneratorConfiguration;
	private JavaTypeResolverConfiguration javaTypeResolverConfiguration;
	/**JDBC配置信息*/
	private JDBCConnectionConfiguration jdbcConnectionConfiguration;
	/**其他类型数据库连接配置信息*/
	private ConnectionFactoryConfiguration connectionFactoryConfigration;
	
	private ArrayList<TableConfiguration> tableConfigurations; 
	
	/** 插件聚合器  */
	private PluginAggregator pluginAggregator;
	
	private List<IntrospectedTable> introspectedTables;
	
	private String introspectedColumnImpl;
	
	private String beginningDelimiter = "\"";
	
	private String endingDelimiter ="\"";
	/**运行环境 db2 oracle mybatis */
	private String targetRuntime;
	
	private JavaFormatter javaFormatter;
	
	public Context(ModelType defaultModelType) {
		if(defaultModelType == null) {
			this.defaultModelType = ModelType.HIERARCHICAL;
		}else {
			this.defaultModelType = defaultModelType;
		}
		tableConfigurations = new ArrayList<TableConfiguration>();
	}
	
	public void introspectTables(ProgressCallBack callback, List<String> warnings, 
			Set<String> fullyQualifiedTableNames) throws SQLException, InterruptedException{
		introspectedTables = new ArrayList<IntrospectedTable>();
		JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this, warnings);
		
		Connection connection = null;
		
		try{
			callback.startTask("startTask");
			connection = getConnection();
			
			DatabaseIntrospector databaseIntrospector 
				= new DatabaseIntrospector(this,connection.getMetaData(),javaTypeResolver,warnings);
			for(TableConfiguration tc : tableConfigurations) {
				String tableName = StringUtil.composeFullyQualifiedTableName(tc.getCatalog(), 
						tc.getSchema(), tc.getTableName(), '.');
				
				if(fullyQualifiedTableNames != null && fullyQualifiedTableNames.size() > 0 
						&& fullyQualifiedTableNames.contains(tableName)) {
					continue;
				}
				if(!tc.areAnyStatementsEnabled()) {
					warnings.add("areAnyStatementsEnabled");
					continue;
				}
				
				List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);
				if(tables != null) {
					introspectedTables.addAll(tables);
				}
				
				callback.checkCancel();
			}
		}finally {
			closeConnection(connection);
		}
	}
	
	private Connection getConnection() throws SQLException {
		ConnectionFactory connectionFactory;
		if(jdbcConnectionConfiguration != null) {
			connectionFactory = new JDBCConnectionFactory(jdbcConnectionConfiguration);
		}else {
			connectionFactory = ObjectFactory.createConnectionFactory(this);
		}
		return connectionFactory.getConnection();
	}
	
	public void closeConnection(Connection connection) {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void generateFiles(ProgressCallBack callback, List<GeneratedJavaFile> generatedJavaFiles,
			List<GeneratedXmlFile> generatedXmlFiles, List<String> warnings) 
			throws InterruptedException{
		
		pluginAggregator = new PluginAggregator();
		
		//从配置里添加额外的插件..
		
		
		if(introspectedTables != null) {
			for(IntrospectedTable introspectedTable : introspectedTables) {
				introspectedTable.initialize();
				/**生成javaModelGenerator并放入缓存中 */
				introspectedTable.calculateGenerators(warnings, callback);
				generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
			}
		}
		
	}
	
	public JavaClientGeneratorConfiguration getJavaClientGeneratorConfiguration() {
		return javaClientGeneratorConfiguration;
	}

	public void setJavaClientGeneratorConfiguration(JavaClientGeneratorConfiguration javaClientGeneratorConfiguration) {
		this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
	}

	public JavaModelGeneratorConfiguration getJavaModelGeneratorConfiguration() {
		return javaModelGeneratorConfiguration;
	}

	public void setJavaModelGeneratorConfiguration(JavaModelGeneratorConfiguration javaModelGeneratorConfiguration) {
		this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
	}

	public JavaTypeResolverConfiguration getJavaTypeResolverConfiguration() {
		return javaTypeResolverConfiguration;
	}

	public void setJdbcConnectionConfiguration(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
		this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
	}

	public ConnectionFactoryConfiguration getConnectionFactoryConfigration() {
		return connectionFactoryConfigration;
	}

	public void setConnectionFactoryConfigration(ConnectionFactoryConfiguration connectionFactoryConfigration) {
		this.connectionFactoryConfigration = connectionFactoryConfigration;
	}

	public String getIntrospectedColumnImpl() {
		return introspectedColumnImpl;
	}

	public void setIntrospectedColumnImpl(String introspectedColumnImpl) {
		this.introspectedColumnImpl = introspectedColumnImpl;
	}

	public String getBeginningDelimiter() {
		return beginningDelimiter;
	}

	public String getEndingDelimiter() {
		return endingDelimiter;
	}

	public String getTargetRuntime() {
		return targetRuntime;
	}

	public void setTargetRuntime(String targetRuntime) {
		this.targetRuntime = targetRuntime;
	}
	
	public JavaFormatter getJavaFormatter() {
		if(javaFormatter == null) {
			javaFormatter = ObjectFactory.createJavaFormatter(this);
		}
		return javaFormatter;
	}
	
	
}
