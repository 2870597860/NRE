package com.entity.processing;
import java.util.ArrayList;
import java.util.Map;

import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 关键词抽取使用示例
 * @author jyzhao,ltian
 *
 */
public class GetKeyWords {
	public ArrayList<String> GetKeyword(String News,int keyWordsNumber) throws Exception{
		
		ArrayList<String> keywords=new ArrayList<String>();
		StopWords sw= new StopWords("models/stopwords");//使用词库停用词等
		CWSTagger seg = new CWSTagger("models/seg.m");//分词器
		AbstractExtractor key = new WordExtract(seg,sw);
		//you need to set the keywords number, here you will get 10 keywords
		Map<String , Integer> ans=key.extract(News, keyWordsNumber);//到提取到的关键字是作为一个Map保存起来的
		//遍历
		//Map的entrySet()方法返回一个实现Map.Entry接口的对象集合。集合中每个对象都是底层Map中一个特定的键/值对
		for (Map.Entry<String, Integer> entry : ans.entrySet()) {
			String keymap=entry.getKey().toString();
			String value=entry.getValue().toString();
			keywords.add(keymap);
			System.out.println("key:"+keymap+" value="+value);
		}
		return keywords;
	}
	
	
}
