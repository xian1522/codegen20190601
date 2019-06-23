package com.wj.codegen.javabean;

import java.util.ArrayList;
import java.util.List;

public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {
	
	private static final String JAVA_LANG = "java.lang";
	
	private String baseShortName;
	private String baseQualifiedName;
	private String packageName;
	
	private List<FullyQualifiedJavaType> typeArguments;
	private boolean explicitlyImported;
	
	private boolean wildcardType;
	private boolean boundedWildcard;
	private boolean extendsBoundedWildcard;
	
	private boolean primitive;
	
	private static FullyQualifiedJavaType booleanPrimitiveInstance = null;
	private static FullyQualifiedJavaType stringInstance = null;
	
	
	public FullyQualifiedJavaType(String fullyTypeSpecification) {
		typeArguments = new ArrayList<FullyQualifiedJavaType>();
		parse(fullyTypeSpecification);
	}
	
	
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
	
	private void parse(String fullTypeSpecification) {
		String spec = fullTypeSpecification.trim();
		
		if(spec.startsWith("?")) {
			wildcardType = true;
			spec = spec.substring(1).trim();
			if(spec.startsWith("extends ")) {
				boundedWildcard = true;
				extendsBoundedWildcard = true;
				spec = spec.substring(8);
			}else if(spec.startsWith("super ")) {
				boundedWildcard = true;
				extendsBoundedWildcard = true;
				spec = spec.substring(6);
			}else {
				boundedWildcard = false;
			}
			parse(spec);
		}else {
			int index = fullTypeSpecification.indexOf('<');
			if(index == -1) {
				simpleParse(fullTypeSpecification);
			}else {
				//simpleParse(fullTypeSpecification.substring(0,index));
			}
		}
	}
	
	public void simpleParse(String typeSpecification) {
		baseQualifiedName = typeSpecification.trim();
		if(baseQualifiedName.contains(".")) {
			packageName = getPackage(baseQualifiedName);
			baseShortName = baseQualifiedName.substring(packageName.length() + 1);
			int index = baseShortName.lastIndexOf('.');
			if(index != -1) {
				baseShortName = baseShortName.substring(index + 1);
			}
			if(JAVA_LANG.equals(packageName)) {
				explicitlyImported = false;
			}else {
				explicitlyImported = true;
			}
		}else {
			baseShortName = baseQualifiedName;
			explicitlyImported = false;
			packageName = "";
			
			if("byte".equals(baseQualifiedName)) {
				primitive = true;
				
			}
		}
	}
	
	private static String getPackage(String baseQualifiedName) {
		int indext = baseQualifiedName.lastIndexOf('.');
		return baseQualifiedName.substring(0, indext);
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
	
	public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
		if(booleanPrimitiveInstance == null) {
			booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean");
		}
		return booleanPrimitiveInstance;
	}
	
	public static final FullyQualifiedJavaType getStringInstance() {
		if(stringInstance == null) {
			stringInstance = new FullyQualifiedJavaType("java.lang.String");
		}
		
		return stringInstance;
	}


	public boolean isPrimitive() {
		return primitive;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		
		if(!(obj instanceof FullyQualifiedJavaType)) {
			return false;
		}
		
		FullyQualifiedJavaType other = (FullyQualifiedJavaType) obj;
		
		return this.getFullyQualifiedName().equals(other.getFullyQualifiedName());
	}
}
