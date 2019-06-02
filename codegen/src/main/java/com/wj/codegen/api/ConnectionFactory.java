package com.wj.codegen.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public interface ConnectionFactory {
	
	Connection getConnection() throws SQLException;
	
	void addConfigurationProperties(Properties properties);
}
