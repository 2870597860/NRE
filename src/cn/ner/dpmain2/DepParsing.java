package cn.ner.dpmain2;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import cn.ner.readwrite.ReadFiles;
import cn.ner.readwrite.WriteContent;


/**
 * 对实体所在的句子进行依存句法分析
 * @author Administrator
 *
 */
public class DepParsing {
	private  TreeSet<String> failSentence=new TreeSet<>();
	public HashMap<String, String> sentenceDp(){
		List<String> fileLists=null;
		String company="";
		HashMap<String, String> sentenceResults=new HashMap<>();
		StringBuffer sbEntitySentences=new StringBuffer();
		try {
			//返回文件的绝对路径
			fileLists = ReadFiles.readDirs("DPCache");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (fileLists.size()>0) {
			for (String file : fileLists){
				int len=file.indexOf("\\DPCache\\");
				company=file.substring(len+"\\DPCache\\".length(),
						file.indexOf(".txt"));
				System.out.println("\n========="+company+":");
				String sentences;
				try {
					sentences = ReadFiles.simpaleReadFiles(file);
					sbEntitySentences.append(sentences);
				}  catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sentenceResults.put(company, sbEntitySentences.toString());
				sbEntitySentences.setLength(0);
			}
		}
		return sentenceResults;
	}
	public void startDP(HashMap<String,String> sentenceResults){
		Set<String> sentenceCompanys=sentenceResults.keySet();
		for (String company : sentenceCompanys) {
			String sentences=sentenceResults.get(company);
			String sentenceDPXml=sentenceDepParsing(company, sentences);
			//将依存分析的xml结果写入文件
			WriteContent wr=new WriteContent();
			String pathXML="./xml/"+company+".xml";
			wr.writeCon(sentenceDPXml, pathXML);
		}
		if (failSentence.size()>0) {
			System.out.println("未处理的公司：");
			for (String sentence : failSentence) {
				System.out.println(sentence);
			}
		}
	}
	public String sentenceDepParsing(String company,String entitySentence){
		StringBuffer sb=null;
		try {
			sb=LTPAPI_dp.sentenceDP(entitySentence);
			System.out.println("处理的公司："+company);
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
		HashMap<String, String> sentenceResults=dep.sentenceDp();
		dep.startDP(sentenceResults);
	}
}
