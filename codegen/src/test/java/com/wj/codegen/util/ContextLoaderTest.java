package com.wj.codegen.util;

import junit.framework.TestCase;

public class ContextLoaderTest extends TestCase {
	
	public void contextLoaderTest() {
		ContextLoader contextLoader = ContextLoader.getInstance();
		String jdbcUrl = contextLoader.getProperties().getProperty("JDBC_URL");
		assertEquals("jdbc:oracle:thin:@192.168.70.121:1521:orcl", jdbcUrl);
	}
}
