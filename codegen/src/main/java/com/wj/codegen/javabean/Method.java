package com.wj.codegen.javabean;

import java.util.List;
import java.util.ListIterator;

import com.wj.codegen.util.OutputUtilities;

public class Method extends JavaElement {
	
	private List<String> bodyLines;
	private boolean constructor;
	private FullyQualifiedJavaType returnType;
	private String name;
	private List<Parameter> parameters;
	private List<FullyQualifiedJavaType> exceptions;
	
	
	public String getFormattedContent(int indentLevel, boolean interfaceMethod, CompilationUnit compilationUnit) {
		StringBuilder sb= new StringBuilder();
		
		OutputUtilities.javaIndent(sb, indentLevel);
		
		if(!interfaceMethod) {
			sb.append(this.getVisibility().getValue());
		}
		if(!constructor) {
			if(this.getReturnType() == null) {
				sb.append("void");
			}else {
				sb.append(this.getReturnType());
			}
			sb.append(' ');
		}
		
		sb.append(this.getName());
		sb.append("(");
		
		boolean comma = false;
		for(Parameter parameter : parameters) {
			if(comma) {
				sb.append(", ");
			}else {
				comma = true;
			}
			sb.append(parameter.getFormattedContent(compilationUnit));
		}
		sb.append(')');
		
		if(exceptions.size() > 0) {
			sb.append("throws ");
			comma = false;
			for(FullyQualifiedJavaType exception : exceptions) {
				if(comma) {
					sb.append(", ");
				}else {
					comma = true;
				}
				sb.append(exception.getShortName());
			}
		}
		
		if(bodyLines.size() == 0) {
			sb.append(';');
		}else {
			sb.append(" {");
			indentLevel++;
			
			ListIterator<String> listIter = bodyLines.listIterator();
			while(listIter.hasNext()) {
				String line = listIter.next();
				if(line.startsWith("}")) {
				indentLevel--;
				}
				OutputUtilities.newLine(sb);
				OutputUtilities.javaIndent(sb, indentLevel);
				sb.append(line);
				
				if((line.endsWith("{")&&!line.startsWith("switch"))
						|| line.endsWith(":")){
					indentLevel++;
				}
				
				if(line.startsWith("break")) {
					if(listIter.hasNext()) {
						String nextLine = listIter.next();
						if(nextLine.startsWith("}")) {
							indentLevel++;
						}
						listIter.previous();
					}
					indentLevel--;
				}
			}
			indentLevel--;
			OutputUtilities.newLine(sb);
			OutputUtilities.javaIndent(sb, indentLevel);
			sb.append('}');
		}
		return sb.toString();
	}
	
	public List<String> getBodyLines(){
		return bodyLines;
	}
	
	public void addBodyLine(String line) {
		bodyLines.add(line);
	}

	public boolean isConstructor() {
		return constructor;
	}

	public void setConstructor(boolean constructor) {
		this.constructor = constructor;
	}

	public FullyQualifiedJavaType getReturnType() {
		return returnType;
	}

	public void setReturnType(FullyQualifiedJavaType returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Parameter> getParameter(){
		return this.parameters;
	}
	
	public void addParameter(Parameter parameter) {
		parameters.add(parameter);
	}

	public List<FullyQualifiedJavaType> getExceptions() {
		return exceptions;
	}
	
	public void addExcetptions(FullyQualifiedJavaType exception){
		exceptions.add(exception);
	}
}
