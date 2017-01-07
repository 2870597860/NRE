package com.entity.processing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.txt.processing.ReadFiles;
/**
 * 
 * @author Administrator
 *
 */
public class RWText {
	//读取testdoing路径下公司年报文本
	public String readText(String company){
		String dirPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing";
		String content=null;
		try {
			List<String> fileLists=ReadFiles.readDirs(dirPath);//返回文件的绝对路径
			for (String file : fileLists) {
				int preCom=file.indexOf("testdoing\\");
				int laterCom=file.indexOf(".txt");
				String Company=file.substring(preCom+"testdoing\\".length(), laterCom);
				if (company.equals(Company)) {
					//读取文件内容
					content=ReadFiles.readFiles(file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("text读取完毕：");
		return content;
	}
}
