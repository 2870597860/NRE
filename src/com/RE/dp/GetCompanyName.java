package com.RE.dp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.txt.processing.ReadFiles;
import com.txt.processing.WriteContent;

public class GetCompanyName {
	public TreeSet<String> getAllCompany(){
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		TreeSet<String> treeSet=new TreeSet<>();
		try {
			List<String> fileLists=ReadFiles.readDirs(entityPath);//返回文件的绝对路径
			for (String file : fileLists) {
				String Company=null;
				int preCom=file.indexOf("entity_");
				int laterCom=file.indexOf(".txt");
				Company=file.substring(preCom+"entity_".length(), laterCom);
				treeSet.add(Company);
				//System.out.println(treeSet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return treeSet;
	}

}
