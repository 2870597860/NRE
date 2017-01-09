package com.entity.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RelationPattern {
	
	/**
	 * 获取语料的信息模板表达式
	 * @return 返回公司以及对应的的实体关系表达式
	 */
	public HashMap<String, List<String>> getCorpusPattern(){
		GetSentences gs=new GetSentences();
		 HashMap<String, HashMap<String, List<String>>> sentenceMap=gs.entitySentenceMap;
		//存放每个公司以及对应的的实体关系表达式
		HashMap<String, List<String>> relationPattern=new HashMap<>();
		List<String> patternRepretation=new ArrayList<>();
		Set<String> companyKey=sentenceMap.keySet();
		for (String everyCompany : companyKey) {
			//获取实体句子，其中包含表格和文本
			HashMap<String, List<String>> flagSentence=sentenceMap.get(everyCompany);
			Set<String> classifications=flagSentence.keySet();
			for (String flag : classifications) {
				//sentenceList是以"||"分开的实体，实体类别和实体句子
				List<String> sentenceList=flagSentence.get(flag);
				if (flag=="biaoge") {
					GetKeyWords gw=new GetKeyWords();
					int keyWordsNumber=10;
					for (String sentence : sentenceList) {
						try {
							//strArr[0]是实体，strArr[1]实体类型，strArr[2]是实体所关联的句子
							String[] strArr=sentence.split("||");
							ArrayList<String> keywords= gw.GetKeyword(strArr[2], keyWordsNumber);
							//...............组成关系表达式................................
							patternRepretation.add(strArr[0]+"||"+strArr[1]+"||"+keywords.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//返回关键词抽取结果list集合
					}
				}else {
					//对实体所在文本进行分析
					SentenceParse sp=new SentenceParse();
					for (String sentence : sentenceList) {
						//strArr[0]是实体，strArr[1]实体类型，strArr[2]是实体所关联的句子
						String[] strArr=sentence.split("||");
						//句子依存语法分析
						ArrayList<String> array=sp.test_dep(strArr[3].replace(" ", ""));
						//...............组成关系表达式................................
						patternRepretation.add(strArr[0]+"||"+strArr[1]+"||"+array.toString());
					}
					
				}
			}
			relationPattern.put(everyCompany, patternRepretation);
		}
		return relationPattern;
	}
}
