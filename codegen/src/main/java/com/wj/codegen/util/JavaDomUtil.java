package com.wj.codegen.util;

import com.wj.codegen.javabean.CompilationUnit;
import com.wj.codegen.javabean.FullyQualifiedJavaType;

public class JavaDomUtil {
	public static String calculateTypeName(CompilationUnit compilationUnit,
			FullyQualifiedJavaType fqjt) {
		if(fqjt.getTypeArguments().size() > 0) {
			
		}
		if(compilationUnit == null
				|| typeDoesNotRequiredImport(fqjt)
				|| typeIsInSamePackage(compilationUnit, fqjt)
				|| typeIsAlreadyImported(compilationUnit, fqjt)) {
			return fqjt.getShortName();
		}else {
			return fqjt.getFullyQualifiedName();
		}
	}
	
	private static boolean typeDoesNotRequiredImport(FullyQualifiedJavaType fullyQualifiedJavaType) {
		return fullyQualifiedJavaType.isExplicitlyImported()
				||fullyQualifiedJavaType.isPrimitive();
	}
	private static boolean typeIsInSamePackage(CompilationUnit compilationUnit,
			FullyQualifiedJavaType fullyQualifiedJavaType) {
		return fullyQualifiedJavaType.getPackageName().equals(compilationUnit.getType().getPackageName());
	}
	private static boolean typeIsAlreadyImported(CompilationUnit compilationUnit,
			FullyQualifiedJavaType fullyQualifiedJavaType) {
		FullyQualifiedJavaType nonGenericType = new FullyQualifiedJavaType(fullyQualifiedJavaType.getFullyQualifiedName());
		return compilationUnit.getImportTypes().contains(nonGenericType);
	}
}
