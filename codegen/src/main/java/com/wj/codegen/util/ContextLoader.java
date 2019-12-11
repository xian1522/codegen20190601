package com.wj.codegen.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
*  配置文件加载
* @Description 
* @author w.j
* @date 2019年12月11日 下午8:00:26
*/
public class ContextLoader {
	
	private static ContextLoader contextLoader = null;
	
	private final String CONFIG_PROPERTIES = "codegenConfig.properties";
	
	private Properties properties;
	
	private ContextLoader() {
		if(properties == null) {
			properties = new Properties();
		}
		init();
	}
	
	public static ContextLoader getInstance() {
		if(contextLoader == null) {
			contextLoader = new ContextLoader();
		}
		return contextLoader;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	private void init() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		InputStream resource = null;
		try {
			resource = cl.getResourceAsStream(CONFIG_PROPERTIES);
			properties.load(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(resource != null) {
				try {
					resource.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
