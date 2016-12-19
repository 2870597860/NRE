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
				
				content=content.substring(content.indexOf("二、主营业务分析"), content.indexOf("十、接待调研"));
				/*String[] str=content.split("五、投资状况");
				for (int i = 0; i < str.length; i++) {
					if(i==1){
						str[i]="五、投资状况"+str[i];
					}
				}*/
				wrt.writeCon(content,path+Company+".txt");
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("处理完毕：");
	}
	/*在java中调用本机python脚本中的函数*/
	/*public static void  usePython(){
		//定义参数因为调用python出现python中模块找不到，也不知道该如何引用，
		 * 所以还是将文本写到文件中，单独调用python程序运行，然后再写到文件中，java在读取。太麻烦了
		PythonInterpreter.initialize(null, null, content);
		PythonInterpreter interpreter = new PythonInterpreter(); 
		//执行
		interpreter.execfile("D:\\A_summer_install\\java\\workspace\\pytest\\src\\InterNameEntityRe.py");
		PyFunction func = (PyFunction)interpreter.get("doing",PyFunction.class);
		PyObject pyobj = func.__call__();
		System.out.println(pyobj.toString());
		try {
			System.out.println("开始执行python：");  
			Process proc = Runtime.getRuntime().exec("python  D:\\A_summer_install\\java\\workspace\\pytest\\src\\InterNameEntityRe.py");
			proc.waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println("python执行结束");
	}*/
}
