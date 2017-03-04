package com.RE.dp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.RE.LTP_Cloud.LTPAPI_dp;
import com.RE.RealtionPattern.DomParse;
import com.RE.writetext.WriteContent;
/**
 * 对实体所在的句子进行依存句法分析
 * @author Administrator
 *
 */
public class DepParsing {
	public static TreeSet<String> failSentence=new TreeSet<>();
	public static TreeMap<String,String> allCompanyEntity=new TreeMap<>();//公司的实体以及实体类型

	public void start(){
		GetCompanyName gcn=new GetCompanyName();
		TreeSet<String> allCompany=gcn.getAllCompany();
		HashMap<String, String> preDpSentence=new HashMap<>();
		for (String everyCompany : allCompany) {
			System.out.println("处理的公司："+everyCompany);
			String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\"+everyCompany+".txt";
			String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity\\entity_"+everyCompany+".txt";
			String cacheSentence=preProcessing(textPath,entityPath);
			System.out.println(cacheSentence);
			preDpSentence.put(everyCompany, cacheSentence);
			String sentenceDPXml=sentenceDepParsing(everyCompany,cacheSentence);
			//将依存分析的xml结果写入文件
			WriteContent wr=new WriteContent();
			String pathXML="./xml/"+everyCompany+".xml";
			wr.writeCon(sentenceDPXml, pathXML);
			//=====================dom解析单独运行
			//List<String> dpSentencelist=DomParse.dp.DOMParseStart(sentenceDPXml);
			/*System.out.println(dpSentencelist);*/
		}
		if (failSentence.size()>0) {
			System.out.println("未处理的公司：");
			for (String sentence : failSentence) {
				System.out.println(sentence);
			}
			//sentenceDepParsing(everyCompany,cacheSentence);
		}
	}
	/**
	 * 
	 * @param textPath 年报文章所在的路径
	 * @param entityPath 实体所在的路径
	 * @return
	 */
	public String preProcessing(String textPath,String entityPath){
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
				allCompanyEntity.put(entityArr[0], entityArr[1]);//搜集所有公司的实体以及实体类型
			}
		}
		HashMap<String, String> mapEntity=new HashMap<>();//<简称,全称>
		for (String string1 : treeSetE) {			
			for (String string2 : treeSetE) {
				if (string1.contains(string2)&&string1.length()>string2.length()) {
					mapEntity.put(string2, string1);//<简称,全称>
				}
			}
		}
		System.out.println(mapEntity);//全称以及对应的简称公司
		int indexStart=text.indexOf("主要控股参股公司情况说明");
		int indexEnd=text.indexOf("公司未来发展的展望");
		FindTextSetence fds=new FindTextSetence();
		TreeSet<String> quChongSentence=new TreeSet<>();
		for (String string : treeSetE) {
			/*if (string.equals("徽商银行")) {
				System.out.println("=======");
			}*/
			if (!allCompanyEntity.get(string).equals("company_name")) {
				continue;
			}
			int indexEntity=text.indexOf(string,indexStart);
			if (indexStart>0) {
				if (indexStart<indexEntity && indexEntity<indexEnd) {
					String entirySentence=fds.findTextSetence(text, string,indexEntity);
					if (!entirySentence.equals("")) {
						Set<String> set=mapEntity.keySet();
						//将公司简称换成全称，并将连续的两个连续公司名删除一个
						for (String jiancheng : set) {
							String quancheng=mapEntity.get(jiancheng);
							if (entirySentence.contains(jiancheng)) {
								if (entirySentence.contains("报告期")) {
									int indexCache=entirySentence.indexOf("报告期");
									entirySentence=entirySentence.substring(indexCache+"报告期".length()+1, entirySentence.length());
								}	
								if (entirySentence.contains(quancheng)) {
									//去除连续相同的实体
									for (String string2 : set) {
										if (entirySentence.contains(quancheng+string2)) {
											entirySentence=entirySentence.replace(quancheng+string2, quancheng);
											break;
										}
										if (entirySentence.contains(string2+quancheng)) {
											entirySentence=entirySentence.replace(string2+quancheng, quancheng);
											break;
										}
									}
								}
								quChongSentence.add(entirySentence);
								break;
							}
						}
					}
					
				}
			}

		}
		StringBuffer sb=new StringBuffer();//存放实体所在的句子
		for (String string : quChongSentence) {
			sb.append(string);
		}
		return sb.toString();
	}
	public String sentenceDepParsing(String company,String entitySentence){
		StringBuffer sb=null;
		try {
			sb=LTPAPI_dp.sentenceDP(entitySentence);
			System.out.println("xml:"+sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			failSentence.add(company);
		}
		if (sb!=null) {
			return sb.toString();
		}
		return "";
	}
	//test
	public static void main(String[] args) {
		DepParsing dep=new DepParsing();
		dep.start();
	}
}
