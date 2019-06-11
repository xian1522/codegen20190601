package com.wj.codegen.config;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
	private List<Context> contexts;
	
	public Configuration() {
		contexts = new ArrayList<Context>();
	}

	public List<Context> getContexts() {
		return contexts;
	}
	
	
}
