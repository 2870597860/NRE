package cn.ner.RealtionPatternMain3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.RE.dp.GetTextEntityAndType;
import com.RE.writetext.WriteContent;
import com.similar.Simhash;

//import src.main.edu.buaa.edu.wordsimilarity.WordSimilarity;

public class GetRelationPatternAndMatch {
	HashMap<String, List<String>> relPatternSeed=new HashMap<>();
	HashMap<String, List<String>> xmlParseResults;
	HashMap<String, HashMap<String,List<String>>> matchResults=new HashMap<>();//key为company，value为<实体，关系>
	//关系模式匹配
	public void matchRelaPatte(){
		getEveryRelDePattAll();//获取关系模式种子
		getTextRelationPattern();//获取文本关系种子
		Set<String> seedRelations=relPatternSeed.keySet();
		Set<String> companys=xmlParseResults.keySet();
		//用来写入文本的集合
		HashMap<String, List<String>> writeParseResults=new HashMap<>();
		//获取公司所有实体和文章用来修正
		GetTextEntityAndType gett=new GetTextEntityAndType();
		gett.getTET();
		for (String company : companys) {
			matchResults.put(company, new HashMap<String,List<String>>());
			List<String> rpLists=xmlParseResults.get(company);
			for (String rpResult : rpLists) {//先读取从语料中获取的关系模式
				//System.out.println(Long.toBinaryString(simhashVal));//输出二进制编码
				String textRP=rpResult.substring(rpResult.indexOf(":")+":".length());
				Simhash simhash = new Simhash(4, 20);//默认按照8段进行simhash存储和汉明距离的衡量标准\
				Long simhashVal = simhash.calSimhash(textRP);
				simhash.store(simhashVal, textRP);
				List<String> changeSeed=new ArrayList<>();
				int minHamm=30;//用于比较并存放最小汉明距离
				String minHammRealtion="";
				String entityCompany="";
				//然后再一次和每一个种子关系下的种子关系模式匹配，汉明距离最小的则判定语料关系模式归为此关系。
				for (String relation : seedRelations) {
					if (!matchResults.get(company).containsKey(relation)) {
						matchResults.get(company).put(relation, new ArrayList<String>());
					}
					List<String> rpArr=relPatternSeed.get(relation);
					for (String seedPattern : rpArr) {
						String seedRP=seedPattern.substring(seedPattern.indexOf(":")+":".length());
						int[] hamm=new int[2];
						hamm[0]=100;
						boolean flag=simhash.isDuplicate(seedRP,hamm);
						//System.out.println(flag);
						if (flag) {
							System.out.println(hamm[0]);
							if (minHamm>hamm[0]) {
								minHamm=hamm[0];
								minHammRealtion=relation;
							}
							//System.out.println("{:::}"+rpResult);
							entityCompany=rpResult.substring(0,rpResult.indexOf(":"));
							//System.out.println("company:"+company);
							//entityCompany=amendEntity(company, entityCompany,gett);
						}
					}
				}
				//System.out.println(rpResult);
				System.out.println("======================================");
				/*
				 * 将从语料中抽取的hamm距离大于8关系模式加入种子模式
				 */
				// 将匹配的结果放入集合中				 
				if (!minHammRealtion.equals("")) {
					if (minHamm>16) {
						relPatternSeed.get(minHammRealtion).add(rpResult);
					}
					matchResults.get(company).get(minHammRealtion).add(entityCompany);
				}
			}
		}
		//进行统计
		countCorrect();
		//将新添加的关系模式写入文本
		writeRelPatternSeed();
	}
	//对每篇文章关系下的实体进行统计
	public void countCorrect(){
		Set<String> tongji=matchResults.keySet();	
		//int countAll=0;
		for (String company : tongji) {			
			System.out.println("\n===================================");
			System.out.println("\n"+company+":::");
			HashMap<String, List<String>> tongjiRelations=matchResults.get(company);
			Set<String> relations=tongjiRelations.keySet();
			for (String relation : relations) {
				System.out.println(relation+":");
				List<String> entitys=tongjiRelations.get(relation);
				int i=0;
				for (String entity : entitys) {
					System.out.print(entity+"          ");
					++i;
					if ((i%4)==0) {
						System.out.println();
					}
				}
				System.out.println("总共有：："+i);
			}
		}
	}
	//将新添加的关系模式写入文本(包含新添加的关系模式)
	public void writeRelPatternSeed(){
		WriteContent wr=new WriteContent();
		StringBuffer sb=new StringBuffer();
		Set<String> relPatterns=relPatternSeed.keySet();
		for (String relPattern : relPatterns) {
			sb.append(relPattern+":\n");
			List<String> patterns=relPatternSeed.get(relPattern);
			for (String pattern : patterns) {
				sb.append(pattern+"\n");
			}
		}
		wr.writeCon(sb.toString(), "./seedPattern/addRelationPattern.txt");
	}
	public String amendEntity(String company,String entityCompany,
			GetTextEntityAndType gett){
		HashMap<String, TreeMap<String,String>> allCompanyEntityType=gett.getAllCompanyEntityType();
		/*HashMap<String, String> allCompanyText=gett.getAllCompanyText();
		Set<String> allCompanys=allCompanyEntityType.keySet()*/;
		TreeMap<String,String> entityAndType=allCompanyEntityType.get(company);
		if (entityAndType==null) {
			System.out.println("========"+company+"没找到！！！");
			return null;
		}
		Set<String> entitys=entityAndType.keySet();
		for (String entity : entitys) {
			if (entity.contains(entityCompany)||entityCompany.contains(entity)) {
				if (entityAndType.get(entity).equals("company_name")) {
					return entity;
				}
				
			}
		}
		return entityCompany;

	}
	//获取依存分析结果的关系模式
	public void getTextRelationPattern(){
		DomParse domPar=new DomParse();
		xmlParseResults=domPar.XMLParseStart("");
		Set<String> set=xmlParseResults.keySet();
		for (String string : set) {
			System.out.println("以上获取关系模式的公司为：："+string);
		}
	}
	//获取种子关系模式
	public void getEveryRelDePattAll(){
		List<String> list=new ArrayList<>();
		StringBuffer sb=readRelationPattern();
		int relIndex=sb.indexOf("附属关系：");
		String pattern=sb.substring(relIndex+"附属关系：\n".length(), sb.indexOf("==",relIndex));
		String[] arr=pattern.split("\n");
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		relPatternSeed.put("附属关系", list);
		//===========================go on		
	}
	public StringBuffer readRelationPattern(){
		String rpPath="seedPattern/relationPattern.txt";
		File file=new File(rpPath);
		StringBuffer sb=new StringBuffer();
		try {
			FileInputStream in=new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(in);
			BufferedReader br=new BufferedReader(isr);
			String line="";
			while ((line=br.readLine())!=null) {
				sb.append(line.replace("<", "").replace(">", "")+"\n");
			}
			br.close();
			isr.close();
			in.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb;
	}
	//测试
	public static void main(String[] args) {
		new GetRelationPatternAndMatch().matchRelaPatte();
	}
}
