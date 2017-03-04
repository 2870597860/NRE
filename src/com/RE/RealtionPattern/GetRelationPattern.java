package com.RE.RealtionPattern;

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
import java.util.TreeSet;

import com.similar.Simhash;

//import src.main.edu.buaa.edu.wordsimilarity.WordSimilarity;

public class GetRelationPattern {
	HashMap<String, List<String>> relPatternSeed=new HashMap<>();
	HashMap<String, List<String>> xmlParseResults;
	HashMap<String, List<String>> matchResults=new HashMap<>();//key为company，value为<实体，关系>
	//关系模式匹配
	public void matchRelaPatte(){
		getEveryRelDePattAll();//获取关系模式种子
		getTextRelationPattern();//获取文本关系种子
		Set<String> relations=relPatternSeed.keySet();
		Set<String> companys=xmlParseResults.keySet();
		Simhash simhash = new Simhash(4, 20);//默认按照8段进行simhash存储和汉明距离的衡量标准
		for (String company : companys) {
			matchResults.put(company, new ArrayList<String>());
			List<String> rpLists=xmlParseResults.get(company);
			for (String rpResult : rpLists) {
				//System.out.println(Long.toBinaryString(simhashVal));//输出二进制编码
				String textRP=rpResult.substring(rpResult.indexOf(":")+":".length());
				Long simhashVal = simhash.calSimhash(textRP);
				simhash.store(simhashVal, textRP);
				List<String> changeSeed=new ArrayList<>();
				int minHamm=30;//用于比较并存放最小汉明距离
				String minHammRealtion="";
				String entityCompany="";
				for (String relation : relations) {
					List<String> rpArr=relPatternSeed.get(relation);
					for (String seedPattern : rpArr) {
						String seedRP=seedPattern.substring(seedPattern.indexOf(":")+":".length());
						int[] hamm=new int[2];
						boolean flag=simhash.isDuplicate(seedRP,hamm);
						if (flag && minHamm>hamm[0]) {
							minHamm=hamm[0];
							minHammRealtion=relation;
							entityCompany=rpResult.substring(0,rpResult.indexOf(":"));
						}
					}
				}
				/*
				 * 将汉明距离大于10的加入种子关系模式
				 */
				if (!minHammRealtion.equals("")) {
					if (minHamm>=10) {
						relPatternSeed.get(minHammRealtion).add(rpResult);
					}
					matchResults.get(company).add(entityCompany+" : "+minHammRealtion);
				}
			}
		}
		System.out.println(matchResults);
	}
	/*//关系模式分割三部分
	public String[] segmentation(String relationPattern){
		String[] segm=new String[4];
		int indexMaohao=relationPattern.indexOf(":");
		int indexSBV=relationPattern.indexOf("SBV");
		String entity=relationPattern.substring(0,indexMaohao);
		segm[0]=entity;
		String sbv=relationPattern.substring(indexMaohao+":".length(), indexSBV+"SBV".length());
		segm[1]=sbv;
		String other="";
		if (relationPattern.indexOf("VOB")>0) {
			String hed=relationPattern.substring(indexSBV+"SBV ".length(),relationPattern.indexOf("VOB"));
			segm[2]=hed;
			other=relationPattern.substring(relationPattern.indexOf("VOB"));
		}
		if (relationPattern.indexOf("POB")>0) {
			String hed=relationPattern.substring(indexSBV+"SBV ".length(),relationPattern.indexOf("POB"));
			segm[2]=hed;
			other=relationPattern.substring(relationPattern.indexOf("POB"));
		}
		segm[3]=other;
		return segm;

	}*/
	//获取依存分析结果的关系模式
	public void getTextRelationPattern(){
		DomParse domPar=new DomParse();
		xmlParseResults=domPar.XMLParseStart("");
		Set<String> set=xmlParseResults.keySet();
		for (String string : set) {
			System.out.println("=="+string+":");
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
		new GetRelationPattern().matchRelaPatte();
	}
}
