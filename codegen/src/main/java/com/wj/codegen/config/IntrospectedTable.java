package com.wj.codegen.config;

import java.util.Map;

import com.wj.codegen.util.StringUtil;

/**
 * 代码生成器实现的基类
 * @author Administrator
 *
 */
public abstract class IntrospectedTable {
	
	protected enum InternalAttribute{
		ATTR_DAO_IMPLEMETATION_TYPE, 
		ATTR_DAO_INTERFACE_TYPE, 
		ATTR_BASE_RECORD_TYPE
	}
	protected Context context;
	protected FullyQualifiedTable fullyQualifiedTable;
	protected Map<IntrospectedTable.InternalAttribute,String>internalAttributes;
	
	
	public void initialize() {
		calculateJavaClientAttributes(); //组装DAO层包名并保存到缓存中
		calculateModelAttributes();//组装model层包名并保存到缓存中
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
		sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
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

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	
}
