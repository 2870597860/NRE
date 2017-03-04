package com.entity.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RelationPattern {

	/**
	 * 获取语料的信息模板表达式
	 * @return 返回公司以及对应的的实体关系表达式
	 */
	public HashMap<String, List<String>> getCorpusPattern(HashMap<String, HashMap<String, List<String>>> sentenceMap){
		//存放每个公司以及对应的的实体关系表达式
		HashMap<String, List<String>> relationPattern=new HashMap<>();
		Set<String> companyKey=sentenceMap.keySet();
		for (String everyCompany : companyKey) {
			List<String> patternRepretation=new ArrayList<>();
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
							String[] strArr=sentence.split("~");
							ArrayList<String>  keywords= gw.GetKeyword(strArr[2], keyWordsNumber);
							//...............组成关系表达式................................
							patternRepretation.add(strArr[0]+"~"+strArr[1]+"~"+keywords.toString());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//返回关键词抽取结果list集合
					}
				}else {
					//对实体所在文本进行分析
					SentenceParse sp=new SentenceParse();
					for (String sentence : sentenceList) {

						/*System.out.println("=============================================");
						System.out.println(sentence);*/
						//strArr[0]是实体，strArr[1]实体类型，strArr[2]是实体所关联的句子
						String[] strArr=sentence.split("~");
						System.out.println(strArr.length);
						for (int i = 0; i < strArr.length; i++) {
							System.out.println(strArr[i]);
						}
						if (strArr.length>2) {
							//句子依存语法分析
							ArrayList<String> array=sp.test_dep(strArr[2].replaceAll(" ", ""));
							//...............组成关系表达式................................
							patternRepretation.add(strArr[0]+"~"+strArr[1]+"~"+array.toString());
						}
					}

				}
			}
			relationPattern.put(everyCompany, patternRepretation);
		}
		return relationPattern;
	}
}
