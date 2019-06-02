package com.wj.codegen.config;

public enum ModelType {
	
	FLAT("flat"),
	HIERARCHICAL("hierarchical");
	
	private final String modelType;
	
	private ModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getModelType() {
		return modelType;
	}
	
	
}
