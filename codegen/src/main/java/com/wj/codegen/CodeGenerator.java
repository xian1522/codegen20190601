package com.wj.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wj.codegen.config.Configuration;
import com.wj.codegen.config.Context;
import com.wj.codegen.exception.ShellException;
import com.wj.codegen.generatefile.GeneratedJavaFile;
import com.wj.codegen.generatefile.callback.DefaultShellCallback;
import com.wj.codegen.generatefile.callback.NullProgressCallBack;
import com.wj.codegen.generatefile.callback.ProgressCallBack;
import com.wj.codegen.generatefile.callback.ShellCallback;
import com.wj.codegen.generatefile.internal.ObjectFactory;

/**
 * 代码生成器
* @Description 
* @author w.j
* @date 2019年5月21日 下午4:10:44
 */
public class CodeGenerator {
	
	private ShellCallback shellCallback;
	private Configuration configuration;
	private List<String> warnings;
	/**待生成的java文件对象 */
	private List<GeneratedJavaFile> generatedJavaFiles;
	/** 意义不明。。*/
	private Set<String> projects;
	
	public CodeGenerator(Configuration configuration, ShellCallback shellCallback,List<String> warnings) {
		if(configuration == null) {
			throw new IllegalArgumentException("configuration is null");
		}else {
			this.configuration = configuration;
		}
		
		if(shellCallback == null) {
			this.shellCallback = new DefaultShellCallback(false);
		}else {
			this.shellCallback = shellCallback;
		}
		if(warnings == null) {
			this.warnings = new ArrayList<String>();
		}else {
			this.warnings = warnings;
		}
		generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
		projects = new HashSet<String>();
	}
	
	public void generate(ProgressCallBack callback,Set<String> contextIds,boolean writeFiles) throws IOException {
		
		if(callback == null) {
			callback = new NullProgressCallBack();
		}
		
		generatedJavaFiles.clear();
		ObjectFactory.reset();
		
		List<Context> contextToRun;
		
		// write or overwrite files start
		if(writeFiles) {
			for(GeneratedJavaFile gjf : generatedJavaFiles) {
				projects.add(gjf.getTargetProject());
				this.writeGeneratedJavaFile(gjf, callback);
			}
		}
	}
	
	/**
	* 写入java文件
	* @Description
	* @user w.j
	* @date 2019年5月22日 下午8:33:58
	* @throws
	*/
	private void writeGeneratedJavaFile(GeneratedJavaFile gjf,ProgressCallBack callback) throws IOException {
		String source = null;
		try {
			File directory = shellCallback.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
			File targetFile = new File(directory,gjf.getFileName());
			if(targetFile.exists()) {
				//待施工..
			}else {
				source = gjf.getFormattedContent();
			}
			this.writeFile(targetFile, source, gjf.getFileEncoding());
		} catch (ShellException e) {
			warnings.add(e.getMessage());
		}
	}
	/**
	* 代码内容写入指定文件
	* @Description
	* @user w.j
	* @date 2019年5月21日 下午5:08:03
	* @throws
	*/
	public void writeFile(File file,String content,String fileEncoding) throws IOException {
		FileOutputStream fos = new FileOutputStream(file,false);
		OutputStreamWriter osw;
		if(fileEncoding == null) {
			osw = new OutputStreamWriter(fos);
		}else {
			osw = new OutputStreamWriter(fos,fileEncoding);
		}
		
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(content);
		bw.close();
	}
}
