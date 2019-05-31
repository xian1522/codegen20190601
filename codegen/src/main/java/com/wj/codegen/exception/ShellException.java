package com.wj.codegen.exception;

public class ShellException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ShellException() {
		super();
	}
	
	public ShellException(String args1) {
		super(args1);
	}
	
	public ShellException(String args1, Throwable args2) {
		super(args1,args2);
	}
	
	public ShellException(Throwable args1) {
		super(args1);
	}
}
