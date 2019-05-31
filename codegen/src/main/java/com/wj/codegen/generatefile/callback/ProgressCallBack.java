package com.wj.codegen.generatefile.callback;

public interface ProgressCallBack {
	
	void introspectionStarted(int totalTasks);
	
	void generationStarted(int totalTasks);
	
	void saveStarted(int totalTasks);
	
	void startTask(String taskName);
	
	void checkCancel() throws InterruptedException;
	
	void done();
}
