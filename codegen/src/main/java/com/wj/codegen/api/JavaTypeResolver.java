package com.wj.codegen.api;

import java.util.List;
import java.util.Properties;

import com.wj.codegen.config.Context;

public interface JavaTypeResolver {
	
	void setWarnings(List<String> warnings);
	
	void addConfigurationProperties(Properties properties);
	
	void setContext(Context context);
}
