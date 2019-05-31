package com.wj.codegen.generatefile.internal;

import java.util.List;
import java.util.Properties;

import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.util.StringUtil;

public class JavaTypeResolverDefaultImpl implements JavaTypeResolver {
	
	protected List<String> warnings;
	protected Properties properties;
	protected boolean forceBigDecimals;
	protected Context context;

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
		forceBigDecimals = StringUtil.isTrue(properties.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
}
