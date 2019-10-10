package com.wj.codegen.javabean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wj.codegen.util.OutputUtilities;

public class InnerCalss extends JavaElement {
	
	private List<Field> fields;
	private List<Method> methods;
	private FullyQualifiedJavaType type;
	private FullyQualifiedJavaType superClass;
	private Set<FullyQualifiedJavaType> superInterfaceTypes;
	
	private boolean isAbstract;
	
	public InnerCalss(FullyQualifiedJavaType type) {
		this.type = type;
		fields = new ArrayList<Field>();
		methods = new ArrayList<Method>();
		superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
	}
	
	public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
		StringBuilder sb = new StringBuilder();
		
		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append(this.getVisibility().getValue());
		
		if(this.isAbstract()) {
			sb.append("abstract ");
		}
		
		sb.append("class ");
		sb.append(this.getType().getShortName());
		
		if(superClass != null) {
			sb.append(" extends ");
			sb.append(superClass.getShortName());
		}
		if(superInterfaceTypes.size() > 0) {
			sb.append(" implements ");
			
			boolean comma = false;
			for(FullyQualifiedJavaType fqjt : superInterfaceTypes) {
				if(comma) {
					sb.append(',');
				}else {
					comma = true;
				}
				sb.append(fqjt.getShortName());
			}
		}
		sb.append(" {");
		indentLevel++;
		
		//添加序列化信息
		OutputUtilities.newLine(sb);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append("private static final long serialVersionUID = 1L;");
		OutputUtilities.newLine(sb);
		
		Iterator<Field> fldIter = fields.iterator();
		while(fldIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Field field = fldIter.next();
			sb.append(field.getFormattedContent(indentLevel, compilationUnit));
			if(fldIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}
		
		if(methods.size() > 0) {
			OutputUtilities.newLine(sb);
		}
		Iterator<Method> mtdIter = methods.iterator();
		while(mtdIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Method method = mtdIter.next();
			sb.append(method.getFormattedContent(indentLevel, false, compilationUnit));
			if(mtdIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}
		
		indentLevel--;
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append('}');
		
		return sb.toString();
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<Method> getMethods() {
		return methods;
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}

	public FullyQualifiedJavaType getType() {
		return type;
	}

	public FullyQualifiedJavaType getSuperClass() {
		return superClass;
	}

	public void setSuperClass(FullyQualifiedJavaType superClass) {
		this.superClass = superClass;
	}

	public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
		return superInterfaceTypes;
	}

	public void addSuperInterfaceTypes(FullyQualifiedJavaType type) {
		superInterfaceTypes.add(type);
	}
	
	public void addField(Field field) {
		this.fields.add(field);
	}
}
