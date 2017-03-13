package com.RE.dp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.python.antlr.PythonParser.comp_op_return;

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

	public void start(){
		GetTextEntityAndType gett=new GetTextEntityAndType();
		gett.getTET();
		HashMap<String, TreeMap<String,String>> allCompanyEntityType=gett.getAllCompanyEntityType();
		HashMap<String, String> allCompanyText=gett.getAllCompanyText();
		Set<String> allCompanys=allCompanyEntityType.keySet();
		System.out.println("总共有："+allCompanys.size()+"个公司！");
		HashMap<String, String> preDpSentence=new HashMap<>();
		for (String comapny : allCompanys) {
			TreeMap<String,String> EntityAndType=allCompanyEntityType.get(comapny);
			String text=allCompanyText.get(comapny);
/*			if (comapny.equals("重庆长安汽车股份有限公司")) {
				System.out.println();
			}*/
			System.out.println(comapny+"句子：：：：：：：");
			String cacheSentence=findEntitySentence(EntityAndType,text);
			String[] fenju=cacheSentence.split("。");
			for (int i = 0; i < fenju.length; i++) {
				System.out.println(fenju[i]);
			}
			preDpSentence.put(comapny, cacheSentence);
			String sentenceDPXml=sentenceDepParsing(comapny,cacheSentence);
			//将依存分析的xml结果写入文件
			WriteContent wr=new WriteContent();
			String pathXML="./xml/"+comapny+".xml";
			wr.writeCon(sentenceDPXml, pathXML);
			//=====================dom解析单独运行
			//List<String> dpSentencelist=DomParse.dp.DOMParseStart(sentenceDPXml);
			//System.out.println(dpSentencelist);
		}
		if (failSentence.size()>0) {
			System.out.println("未处理的公司：");
			for (String sentence : failSentence) {
				System.out.println(sentence);
			}
			//sentenceDepParsing(everyCompany,cacheSentence);
		}
	}
	public String findEntitySentence(TreeMap<String,String> EntityAndType,String text){
		HashMap<String, String> mapEntity=new HashMap<>();//<简称,全称>
		Set<String> entitys=EntityAndType.keySet();
		int indexStart=text.indexOf("主要控股参股公司情况说明");
		int indexEnd=text.indexOf("公司未来发展的展望");
		FindTextSetence fds=new FindTextSetence();
		TreeSet<String> quChongSentence=new TreeSet<>();
		for (String entity1 : entitys) {
			//System.out.println("=="+entity1);
			for (String entity2 : entitys) {
				if (entity1.contains(entity2)&& entity1.length()>entity2.length()) {
					mapEntity.put(entity2, entity1);//<简称,全称>
					continue;
				}
			}
			mapEntity.put(entity1,null);
		}
		for (String entity1 : entitys) {
			if (!EntityAndType.get(entity1).equals("company_name")) {
				continue;
			}
			int indexEntity=text.indexOf(entity1,indexStart);
			if (indexStart<0) {
				indexStart=text.indexOf("主要控股参股公司分析");
			}
			if (indexStart>0) {
				if (indexStart<indexEntity && indexEntity<indexEnd) {
					if((text.substring(indexEntity+entity1.length(), indexEntity+entity1.length()+1).equals(" "))&&(text.substring(indexEntity-1, indexEntity).equals(" "))){
						System.out.println(entity1);
						System.out.println("=====");
						continue;//是表格
					}
					String entirySentence=fds.findTextSetence(text, entity1,indexEntity);
					if (!entirySentence.equals("")) {
						Set<String> set=mapEntity.keySet();
						//将公司简称换成全称，并将连续的两个连续公司名删除一个
						for (String jiancheng : set) {
							String quancheng=mapEntity.get(jiancheng);
							if (quancheng!=null) {
								if (entirySentence.contains(jiancheng)) {
									if (entirySentence.contains("报告期，")) {
										int indexCache=entirySentence.indexOf("报告期");
										entirySentence=entirySentence.substring(indexCache+"报告期".length()+1);
									}	
									if (entirySentence.contains("报告期内，")) {
										int indexCache=entirySentence.indexOf("报告期内");
										entirySentence=entirySentence.substring(indexCache+"报告期内".length()+1);
									}	
									if ((quancheng!=null) && (entirySentence.contains(quancheng))) {
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
									
								}
							}
							quChongSentence.add(entirySentence);
							break;
						}
					}

				}
			}
		}
		System.out.println(mapEntity);
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
		dep.start();
	}
}
