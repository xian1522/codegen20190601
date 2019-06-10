package com.wj.codegen.generatefile.oracle;

import java.util.ArrayList;
import java.util.List;

import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.generatefile.GeneratedJavaFile;
import com.wj.codegen.generatefile.callback.ProgressCallBack;
import com.wj.codegen.generatefile.oracle.model.ExampleGenerator;
import com.wj.codegen.javabean.CompilationUnit;

public class IntrospectedTableOracleImpl extends IntrospectedTable {
	
	protected List<AbstractJavaGenerator> javaModelGenerators;
	protected List<AbstractJavaGenerator> clientGenerators;
	
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
		
	}
	
	protected void calculateJavaModelGenerators(List<String>warnings,ProgressCallBack progressCallback) {
		if(this.getRules().generateExampleClass()) {
			AbstractJavaGenerator javaGenerator = new ExampleGenerator();
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

}
