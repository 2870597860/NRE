package com.RE.dp;

import java.util.TreeMap;
import java.util.TreeSet;

public class GetEntityAndType {
	public TreeMap<String,String> getET(){
		TreeMap<String,String> allCompanyEntity_type=new TreeMap<>();
		TreeSet<String> allCompany=getAllCompany();
		for (String everyCompany : allCompany) {
			System.out.println("处理的公司："+everyCompany);
			String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\"+everyCompany+".txt";
			String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity\\entity_"+everyCompany+".txt";
			getAllCompanyEntityType(textPath,entityPath,allCompanyEntity_type);
		}
		return allCompanyEntity_type;
	}
	public TreeSet<String> getAllCompany(){
		GetCompanyName gcn=new GetCompanyName();
		TreeSet<String> allCompany=gcn.getAllCompany();
		return allCompany;
	}
	public void getAllCompanyEntityType(String textPath,String entityPath,
			TreeMap<String,String> allCompanyEntity_type){
		ReadTXTEntity rte=new ReadTXTEntity();
		String entityContent=rte.readText(entityPath);
		TreeSet<String> treeSetEntityArr=rte.getFilter(entityContent);
		for (String string : treeSetEntityArr) {
			String[] entityArr=string.split("、");
			if (entityArr[0].length()>=3) {
				allCompanyEntity_type.put(entityArr[0], entityArr[1]);//搜集所有公司的实体以及实体类型
			}
		}
	}
}
