package com.wj.codegen.javabean;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import com.wj.codegen.util.OutputUtilities;
import com.wj.codegen.util.StringUtil;

public class TopLevelClass extends InnerCalss implements CompilationUnit {
	
	private Set<FullyQualifiedJavaType> importedTypes;
	
	public TopLevelClass(FullyQualifiedJavaType type) {
		super(type);
		//这里为什么要用TreeSet?
		importedTypes = new TreeSet<FullyQualifiedJavaType>();
	}
	
	public String getFormattedContent() {
		StringBuilder sb = new StringBuilder();
		
		if(StringUtil.stringHasValue(this.getType().getPackageName())) {
			sb.append("package ");
			sb.append(getType().getPackageName());
			sb.append(';');
			OutputUtilities.newLine(sb);
			OutputUtilities.newLine(sb);
		}
		
		Set<String> importStrings = OutputUtilities.calculateImports(importedTypes);
		for(String importString : importStrings) {
			sb.append(importString);
			OutputUtilities.newLine(sb);
		}
		if(importStrings.size() > 0) {
			OutputUtilities.newLine(sb);
		}
		
		sb.append(super.getFormattedContent(0, this));
		
		return sb.toString();
	}

	public Set<FullyQualifiedJavaType> getImportTypes() {
		return Collections.unmodifiableSet(this.importedTypes);
	}

	public Set<String> getStaticImports() {
		return null;
	}

	public boolean isJavaInterface() {
		return false;
	}

	public boolean isJavaEnumeration() {
		return false;
	}
	
	public Set<FullyQualifiedJavaType> getImportedTypes(){
		return importedTypes;
	}
	
	public void addImportedTypes(FullyQualifiedJavaType type) {
		importedTypes.add(type);
	}
	
	public void addImportedType(FullyQualifiedJavaType importedType) {
		if(importedType != null 
				&& !importedType.isExplicitlyImported()
				&& !importedType.getPackageName().equals(this.getType().getPackageName())
				&& !importedType.getShortName().equals(this.getType().getShortName())) {
			importedTypes.add(importedType);
		}
	}
}
