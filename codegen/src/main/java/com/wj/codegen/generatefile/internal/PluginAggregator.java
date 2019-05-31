package com.wj.codegen.generatefile.internal;

import java.util.ArrayList;
import java.util.List;

public class PluginAggregator implements Plugin {
	
	private List<Plugin> plugins;
	
	public PluginAggregator() {
		plugins = new ArrayList<Plugin>();
	}
	
	public void addPlugin(Plugin plugin) {
		plugins.add(plugin);
	}
}
