package com.txt.processing;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
/**
 * 对原文进行过滤，去除不必要的部分,
 * 将需要的部分写入另一文本中，为python的entity识别做准备
 * testdoing中存放待处理的年报文本，
 * writeContent.java中CacheContent为处理后写入存储的路径
 * @author Administrator
 *
 */
public class TxtProcessing {
	public static void main(String[] args) {
		String dirPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing";//读取文件路径
		String path="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\CacheContent\\";//写入文件路径
		String content=null;
		WriteContent wrt=new WriteContent();
		try {
			List<String> fileLists=ReadFiles.readDirs(dirPath);//返回文件的绝对路径
			for (String file : fileLists) {
				int preCom=file.indexOf("\\testdoing");
				int laterCom=file.indexOf(".txt");
				String Company=file.substring(preCom+"testdoing\\".length(), laterCom);
				//读取文件内容
				content=ReadFiles.readFiles(file);
				int textStart=content.indexOf("主营业务分析");
				int textEnd=content.indexOf("十、接待调研");
				if (textStart<0) {
					textStart=0;
				}
				if (textEnd<0) {
					textEnd=content.indexOf("可能面对的风险");
					if (textEnd<0) {
						textEnd=content.length();
					}
				}
				content=content.substring(textStart, textEnd);
				wrt.writeCon(content,path+Company+".txt");
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("处理完毕：");
	}
}
