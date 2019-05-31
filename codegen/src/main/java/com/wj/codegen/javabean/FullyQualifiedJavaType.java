package com.wj.codegen.javabean;

import java.util.ArrayList;
import java.util.List;

public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {
	
	private String baseShortName;
	private String baseQualifiedName;
	private String packageName;
	
	private List<FullyQualifiedJavaType> typeArguments;
	private boolean explicitlyImported;
	
	
	
	
	public List<String> getImportList(){
		List<String> answer = new ArrayList<String>();
		if(this.isExplicitlyImported()) {
			int index = baseShortName.indexOf('.');
			if(index == -1) {
				answer.add(baseQualifiedName);
			}else {
				StringBuilder sb = new StringBuilder();
				sb.append("packageName");
				sb.append('.');
				sb.append(baseShortName.subSequence(0, index));
				answer.add(sb.toString());
			}
		}
		for(FullyQualifiedJavaType fqjt : typeArguments) {
			answer.addAll(fqjt.getImportList());
		}
		return answer;
	}
	
	public String getShortName() {
		return baseShortName;
	}
	
	public String getShortNameWithoutTypeArguments() {
		return baseShortName;
	}
	
	public String getFullyQualifiedName() {
		return baseQualifiedName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public int compareTo(FullyQualifiedJavaType o) {
		return 0;
	}

	public List<FullyQualifiedJavaType> getTypeArguments() {
		return typeArguments;
	}

	public boolean isExplicitlyImported() {
		return explicitlyImported;
	}
	
	
}
