package com.RE.dp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import com.txt.processing.ReadFiles;
/**
 * 获取所有公司对饮的文本以及实体、实体类型
 * @author Administrator
 *
 */
public class GetTextEntityAndType {
	private HashMap<String, TreeMap<String,String>> allCompanyEntityType=new HashMap<>();
	private HashMap<String, String> allCompanyText=new HashMap<>();
	
	public HashMap<String, TreeMap<String, String>> getAllCompanyEntityType() {
		return allCompanyEntityType;
	}
	public HashMap<String, String> getAllCompanyText() {
		return allCompanyText;
	}
	//输入所有公司集合，获取每个公司对应的文本以及实体和类型
	public void getTET(){	
		TreeSet<String> allCompany=getAllCompany();
		ReadTXTEntity rte=new ReadTXTEntity();
		for (String everyCompany : allCompany) {
			String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\"+everyCompany+".txt";
			String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity\\entity_"+everyCompany+".txt";
			/*if (everyCompany.equals("深圳市得润电子股份有限公司")) {
				System.out.println();
			}*/
			TreeMap<String,String> Entity_type=getCEType(textPath,entityPath,rte);
			
			allCompanyEntityType.put(everyCompany, Entity_type);
			String text=rte.readText(textPath);//读取文本
			allCompanyText.put(everyCompany, text);
		}
	}
	private TreeMap<String,String> getCEType(String textPath,String entityPath,
			ReadTXTEntity rte){
		String entityContent=rte.readText(entityPath);
		TreeSet<String> treeSetEntityArr=rte.getFilter(entityContent);//只获取公司实体和产品实体
		TreeMap<String,String> Entity_type=new TreeMap<>();
		for (String string : treeSetEntityArr) {
			String[] entityArr=string.split("、");
			if ( entityArr[0].matches("[a-zA-Z]+") && entityArr[0].length()>5) {
				Entity_type.put(entityArr[0], entityArr[1]);
			}else if ( entityArr[0].matches("[\u4E00-\u9FA5]+") && entityArr[0].length()>=3) {
				Entity_type.put(entityArr[0], entityArr[1]);
				
			}else if (entityArr[0].length()>5) {
				Entity_type.put(entityArr[0], entityArr[1]);
			}
		}
		return Entity_type;
	}
	private TreeSet<String> getAllCompany(){
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		TreeSet<String> allCompany=new TreeSet<>();
		try {
			List<String> fileLists=ReadFiles.readDirs(entityPath);//返回文件的绝对路径
			for (String file : fileLists) {
				String Company=null;
				int preCom=file.indexOf("entity_");
				int laterCom=file.indexOf(".txt");
				Company=file.substring(preCom+"entity_".length(), laterCom);
				allCompany.add(Company);
				//System.out.println(treeSet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allCompany;
	}
}
