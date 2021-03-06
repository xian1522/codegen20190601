package com.wj.codegen.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mysql.jdbc.StringUtils;
import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.generatefile.GeneratedJavaFile;
import com.wj.codegen.generatefile.GeneratedXmlFile;
import com.wj.codegen.generatefile.callback.ProgressCallBack;
import com.wj.codegen.generatefile.internal.rules.HierarchicalModelRules;
import com.wj.codegen.generatefile.internal.rules.Rules;
import com.wj.codegen.util.StringUtil;

/**
 * 代码生成器实现的基类
 * @author Administrator
 *
 */
public abstract class IntrospectedTable {
	
	public enum TargetRuntime{
		ORACLE
	}
	
	protected enum InternalAttribute{
		ATTR_DAO_IMPLEMETATION_TYPE, 
		ATTR_DAO_INTERFACE_TYPE, 
		ATTR_BASE_RECORD_TYPE,
		
		ATTR_EXAMPLE_TYPE, ATTR_ORACLE_XML_MAPPER_FILE_NAME, ATTR_ORACLE_XML_MAPPER_PACKAGE,
	}
	protected Context context;
	protected TableConfiguration tableConfiguration;
	protected FullyQualifiedTable fullyQualifiedTable;
	protected Map<IntrospectedTable.InternalAttribute,String>internalAttributes;
	
	protected List<IntrospectedColumn> blobColumns;
	protected List<IntrospectedColumn> baseColumns;
	protected List<IntrospectedColumn> primaryKeyColumns;
	
	protected String remark;
	protected String tableType;
	
	protected Rules rules;
	
	
	public void initialize() {
		calculateJavaClientAttributes(); //组装DAO层包名并保存到缓存中
		calculateModelAttributes();//组装model层包名并保存到缓存中
		calculateXmlAttributes(); //初始化XML配置
		
		if(tableConfiguration.getModelType() == ModelType.HIERARCHICAL) {
			rules = new HierarchicalModelRules(this);
		}
		
	}
	/**
	* 初始化XML属性配置
	* @Description
	* @user w.j
	* @date 2019年9月11日 上午8:09:32
	* @throws
	 */
	protected void calculateXmlAttributes() {
		setOracleXmlPackage(calculateXmlPackage());
		setOracleXmlFileName(calculateOracleXmlFileName());
	}
	
	protected String calculateOracleXmlFileName() {
		StringBuilder sb = new StringBuilder();
		if(StringUtil.stringHasValue(tableConfiguration.getMapperName())) {
			String mapperName = tableConfiguration.getMapperName();
			int ind = mapperName.lastIndexOf('.');
			if(ind == -1) {
				sb.append(mapperName);
			}else {
				sb.append(mapperName.substring(ind + 1));
			}
			sb.append(".hbm.xml");
		}else {
			sb.append(fullyQualifiedTable.getDomainObjectName());
			sb.append(".hbm.xml");
		}
		return sb.toString();
	}
	
	public void setOracleXmlFileName(String oracleXmlFileName) {
		internalAttributes.put(InternalAttribute.ATTR_ORACLE_XML_MAPPER_FILE_NAME, oracleXmlFileName);
	}
	
	protected String calculateXmlPackage() {
		StringBuilder sb = new StringBuilder();
		SqlMapGeneratorConfiguration config = context.getSqlMapGeneratroConfiguration();
		
		if(config != null) {
			sb.append(config.getTargetPackage());
		//	sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
			if(StringUtil.stringHasValue(tableConfiguration.getMapperName())) {
				String mapperName = tableConfiguration.getMapperName();
				int ind = mapperName.lastIndexOf(".");
				if(ind != -1) {
					sb.append('.').append(mapperName.substring(0, ind));
				}
			}
		}
		return sb.toString();
	}
	
	public void setOracleXmlPackage(String oracleXmlPackage) {
		internalAttributes.put(InternalAttribute.ATTR_ORACLE_XML_MAPPER_PACKAGE, oracleXmlPackage);
	}
	
	public abstract void calculateGenerators(List<String> warnigns,ProgressCallBack progressCallback); 
	
	public IntrospectedTable(TargetRuntime targetRuntime) {
		primaryKeyColumns = new ArrayList<IntrospectedColumn>();
		blobColumns = new ArrayList<IntrospectedColumn>();
		baseColumns = new ArrayList<IntrospectedColumn>();
		internalAttributes = new HashMap<IntrospectedTable.InternalAttribute,String>();
	}
	
	protected void calculateModelAttributes() {
		String pakkage = this.calculateJavaModelPackage();
		
		StringBuilder sb = new StringBuilder();
		sb.append(pakkage);
		sb.append('.');
		sb.append(fullyQualifiedTable.getDomainObjectName());
		setBaseRecordType(sb.toString());
	}
	/**
	 *  处理包名（Dao.impl）
	* @Description
	* @user w.j
	* @date 2019年4月16日 下午11:17:09
	* @throws
	 */
	protected void calculateJavaClientAttributes() {
		if(context.getJavaClientGeneratorConfiguration() == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(this.calculateJavaClientImplementationPackage());
		sb.append('.');
		sb.append(fullyQualifiedTable.getDomainObjectName());
		sb.append("DAOImpl");
		setDAOImplementationType(sb.toString());
		
		sb.setLength(0);
		sb.append(calculateJavaClientInterfacePackage());
		sb.append(fullyQualifiedTable.getDomainObjectName());
		sb.append("DAO");
		setDaoInterfaceType(sb.toString());
		
		
	}
	
	protected String calculateJavaModelPackage() {
		JavaModelGeneratorConfiguration config = context.getJavaModelGeneratorConfiguration();
		if(config == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(config.getTargetPackage());
		//去除schema包名
	//	sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
		return sb.toString();
	}
	
	
	protected String calculateJavaClientInterfacePackage() {
		JavaClientGeneratorConfiguration config = context.getJavaClientGeneratorConfiguration();
		if(config == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(config.getTargetPackage());
		sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
		return sb.toString();
	}
	
	protected String calculateJavaClientImplementationPackage() {
		JavaClientGeneratorConfiguration config = context.getJavaClientGeneratorConfiguration();
		if(config == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if(StringUtil.stringHasValue(config.getImplementationPackage())) {
			sb.append(config.getImplementationPackage());
		}else {
			sb.append(config.getTargetPackage());
		}
		sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
		return sb.toString();
	}
	
	public void addColumn(IntrospectedColumn introspectedColumn) {
		if(introspectedColumn.isBLOBColumn()) {
			blobColumns.add(introspectedColumn);
		}else {
			baseColumns.add(introspectedColumn);
		}
		introspectedColumn.setIntrospectedTable(this);
	}
	
	public void addPrimaryKeyColumn(String columnName) {
		boolean found = false;
		Iterator<IntrospectedColumn> iter = baseColumns.iterator();
		while(iter.hasNext()) {
			IntrospectedColumn column = iter.next();
			if(column.getActualColumnName().equals(columnName)) {
				primaryKeyColumns.add(column);
				iter.remove();
				found = true;
				break;
			}
		}
		
		if(!found) {
			iter = blobColumns.iterator();
			while(iter.hasNext()) {
				IntrospectedColumn column = iter.next();
				if(column.getActualColumnName().equals(columnName)) {
					primaryKeyColumns.add(column);
					iter.remove();
					found = true;
					break;
				}
			}
		}
	}
	
	public boolean isConstructorBased() {
		if(isImmutable()) {
			return true;
		}
		
		Properties properties;
		if(tableConfiguration.getProperties().containsKey(PropertyRegistry.ANY_CONSTRUCTOR_BASED)) {
			properties = tableConfiguration.getProperties();
		}else {
			properties = context.getJavaClientGeneratorConfiguration().getProperties();
		}
		return StringUtil.isTrue(properties.getProperty(PropertyRegistry.ANY_CONSTRUCTOR_BASED));
	}
	
	/** 
	 * checks if is immutable
	 * @return
	 */
	public boolean isImmutable() {
		Properties properties;
		
		if(tableConfiguration.getProperties().containsKey(PropertyRegistry.ANY_IMMUTABLE)) {
			properties = tableConfiguration.getProperties();
		}else {
			properties = context.getJavaClientGeneratorConfiguration().getProperties();
		}
		return StringUtil.isTrue(properties.getProperty(PropertyRegistry.ANY_IMMUTABLE));
	}
	
	public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();
	
	public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();
	
	private boolean isSubPackagesEnabled(PropertyHolder propertyHolder) {
		return StringUtil.isTrue(propertyHolder.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES));
	}
	
	public void setDAOImplementationType(String DAOImplementationType) {
		internalAttributes.put(InternalAttribute.ATTR_DAO_IMPLEMETATION_TYPE, DAOImplementationType);
	}
	
	public void setDaoInterfaceType(String DAOInterfaceType) {
		internalAttributes.put(InternalAttribute.ATTR_DAO_INTERFACE_TYPE, DAOInterfaceType);
	}
	
	public void setBaseRecordType(String baseRecordType) {
		internalAttributes.put(InternalAttribute.ATTR_BASE_RECORD_TYPE, baseRecordType);
	}
	
	public String getBaseRecordType() {
		return internalAttributes.get(InternalAttribute.ATTR_BASE_RECORD_TYPE);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public FullyQualifiedTable getFullyQualifiedTable() {
		return fullyQualifiedTable;
	}

	public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
		this.fullyQualifiedTable = fullyQualifiedTable;
	}

	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public void setTableConfiguration(TableConfiguration tableConfiguration) {
		this.tableConfiguration = tableConfiguration;
	}

	public List<IntrospectedColumn> getBlobColumns() {
		return blobColumns;
	}

	public List<IntrospectedColumn> getBaseColumns() {
		return baseColumns;
	}

	public List<IntrospectedColumn> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public Rules getRules() {
		return rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}
	
	public String getExampleType() {
		return internalAttributes.get(InternalAttribute.ATTR_EXAMPLE_TYPE);
	}
	
	public void setExampleType(String exampleType) {
		internalAttributes.put(InternalAttribute.ATTR_EXAMPLE_TYPE, exampleType);
	}
	
	public String getTableConfigurationProperty(String property) {
		return tableConfiguration.getProperty(property);
	}
	
	public List<IntrospectedColumn> getAllColumns(){
		List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
		answer.addAll(primaryKeyColumns);
		answer.addAll(baseColumns);
		answer.addAll(blobColumns);
		return answer;
	}
	
	public boolean hasPrimaryKeyColumns() {
		return primaryKeyColumns.size() > 0;
	}
	
	public abstract String getXmlMapperFileName();
	
	public abstract String getXmlMapperPackage();
}
