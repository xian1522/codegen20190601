package com.wj.codegen.generatefile.internal;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.wj.codegen.api.ConnectionFactory;
import com.wj.codegen.config.JDBCConnectionConfiguration;
import com.wj.codegen.util.StringUtil;

public class JDBCConnectionFactory implements ConnectionFactory {
	
	private String userId;
	private String password;
	private String connectionURL;
	private String driverClass;
	private Properties otherProperties;
	
	public JDBCConnectionFactory(JDBCConnectionConfiguration config) {
		userId = config.getUserId();
		password = config.getPassword();
		connectionURL = config.getConnectionURL();
		driverClass = config.getDriverClass();
		otherProperties = config.getProperties();
	}

	public Connection getConnection() throws SQLException {
		Driver driver = this.getDriver();
		
		Properties props = new Properties();
		if(StringUtil.stringHasValue(userId)) {
			props.setProperty("user", userId);
		}
		if(StringUtil.stringHasValue(password)) {
			props.setProperty("password", password);
		}
		props.putAll(otherProperties);
		
		Connection conn = driver.connect(connectionURL, props);
		if(conn == null) {
			throw new SQLException("");
		}
		return conn;
	}

	public void addConfigurationProperties(Properties properties) {
		// TODO Auto-generated method stub

	}
	
	private Driver getDriver() {
		Driver driver;
		try {
			Class<?> clazz = ObjectFactory.externalClassForName(driverClass);
			driver = (Driver) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("");
		}
		return driver;
	}

}
