package com.wj.codegen.generatefile.oracle;

import java.util.ArrayList;
import java.util.List;

import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.generatefile.GeneratedJavaFile;
import com.wj.codegen.generatefile.GeneratedXmlFile;
import com.wj.codegen.generatefile.callback.ProgressCallBack;
import com.wj.codegen.generatefile.internal.ObjectFactory;
import com.wj.codegen.generatefile.oracle.javamapper.SimpleJavaClientGenerator;
import com.wj.codegen.generatefile.oracle.model.SimpleModelGenerator;
import com.wj.codegen.javabean.CompilationUnit;

public class IntrospectedTableOracleImpl extends IntrospectedTable {
	
	protected List<AbstractJavaGenerator> javaModelGenerators;
	protected List<AbstractJavaGenerator> clientGenerators;
	protected AbstractXmlGenerator xmlMapperGenerator;
	
	public IntrospectedTableOracleImpl() {
		super(TargetRuntime.ORACLE);
		javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
		clientGenerators = new ArrayList<AbstractJavaGenerator>();
	}

	@Override
	public List<GeneratedJavaFile> getGeneratedJavaFiles() {
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		
		for(AbstractJavaGenerator javaGenerator : javaModelGenerators) {
			List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
			for(CompilationUnit compilationUnit : compilationUnits) {
				GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
						context.getJavaModelGeneratorConfiguration().getTargetProject(),
						context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
						context.getJavaFormatter());
				answer.add(gjf);
			}
		}
		
		for (AbstractJavaGenerator javaGenerator : clientGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator
                    .getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        context.getJavaClientGeneratorConfiguration()
                                .getTargetProject(),
                                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                                context.getJavaFormatter());
                answer.add(gjf);
            }
        }
		return answer;
	}

	@Override
	public void calculateGenerators(List<String> warnings, ProgressCallBack progressCallback) {
		/**构建javaModelGenerator并放入缓存中 */
		calculateJavaModelGenerators(warnings,progressCallback);
		
		AbstractJavaClientGenerator javaClientGenerator = calculateClientGenerators(warnings,progressCallback);
		
		calculateXmlMapperGenerator(javaClientGenerator,warnings,progressCallback);
		
	}
	
	protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings,
			ProgressCallBack progressCallback) {
		if(!rules.generatedJavaClient()) {
			return null;
		}
		AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
		if(javaGenerator == null) {
			return null;
		}
		
		return javaGenerator;
	}
	
	protected AbstractJavaClientGenerator createJavaClientGenerator() {
		if(context.getJavaClientGeneratorConfiguration() == null) {
			return null;
		}
		
		String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();
		AbstractJavaClientGenerator javaGenerator;
		if("XMLMAPPER".equalsIgnoreCase(type)) {
			javaGenerator = new SimpleJavaClientGenerator();
		}else if("ANNOTATEDMAPPER".equalsIgnoreCase(type)) {
			javaGenerator = null;
		}else {
			javaGenerator =(AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
		}
		return javaGenerator;
	}
	
	protected void calculateJavaModelGenerators(List<String>warnings,ProgressCallBack progressCallback) {
		if(this.getRules().generateExampleClass()) {
			AbstractJavaGenerator javaGenerator = new SimpleModelGenerator();
			this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
			javaModelGenerators.add(javaGenerator);
		}
	}
	
	protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator,List<String> warnings,
			ProgressCallBack progressCallback) {
		if(abstractGenerator == null) {
			return;
		}
		abstractGenerator.setContext(context);
		abstractGenerator.setIntrospectedTable(this);
		abstractGenerator.setProgressCallback(progressCallback);
		abstractGenerator.setWarnings(warnings);
	}
	
	protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warngings,ProgressCallBack callback) {
		if(javaClientGenerator == null) {
			
		}else {
			xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
		}
	}

	@Override
	public List<GeneratedXmlFile> getGeneratedXmlFiles() {
		return null;
	}
	
	

}
