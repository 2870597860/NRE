package com.RE.RealtionPattern;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.RE.dp.GetCompanyName;
import com.RE.dp.ReadTXTEntity;

public class GetEnType {
	public TreeMap<String,String> startRead(String company){
		GetCompanyName gcn=new GetCompanyName();
		TreeSet<String> allCompany=gcn.getAllCompany();
		TreeMap<String,String> allCompanyType=new TreeMap<>();//公司的实体以及实体类型
		for (String everyCompany : allCompany) {
			if (company.equals(everyCompany)) {
				getCompanyType(everyCompany,allCompanyType);
			}
		}
		return allCompanyType;
	}
	public void getCompanyType(String everyCompany,TreeMap<String,String> allCompanyType){
		String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\"+everyCompany+".txt";
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity\\entity_"+everyCompany+".txt";
		ReadTXTEntity rte=new ReadTXTEntity();
		String text=rte.readText(textPath);
		//System.out.println(text);
		String entityContent=rte.readText(entityPath);
		TreeSet<String> treeSetEntityArr=rte.getFilter(entityContent);
		TreeSet<String> treeSetE=new TreeSet<>();//存放去掉过短的实体
		for (String string : treeSetEntityArr) {
			String[] entityArr=string.split("、");
			if (entityArr[0].length()>=3) {
				treeSetE.add(entityArr[0]);//treeSetE精简过的实体
				allCompanyType.put(entityArr[0], entityArr[1]);//搜集所有公司的实体以及实体类型
			}
		}
	}
}
