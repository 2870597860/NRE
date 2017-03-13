package cn.ner.preprocession;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 关键词抽取使用示例
 * @author jyzhao,ltian
 *
 */
public class GetKeyWords {
	public StringBuffer GetKeyword(String News,int keyWordsNumber) throws Exception{
		HashMap<String, Integer> keywordsvalue=new HashMap<>();
		StopWords sw= new StopWords("models/stopwords");//使用词库停用词等
		CWSTagger seg = new CWSTagger("./models/seg.m", new Dictionary("./models/dict.txt"));
		AbstractExtractor key = new WordExtract(seg,sw);
		//you need to set the keywords number, here you will get 10 keywords
		Map<String , Integer> ans=key.extract(News, keyWordsNumber);//到提取到的关键字是作为一个Map保存起来的
		//遍历
		//Map的entrySet()方法返回一个实现Map.Entry接口的对象集合。集合中每个对象都是底层Map中一个特定的键/值对
		double vv=0;	
		for (Map.Entry<String, Integer> entry : ans.entrySet()) {
			String keymap=entry.getKey().toString();
			int value=entry.getValue();
			keywordsvalue.put(keymap, value);
			System.out.println("key:"+keymap+" value="+value);
			vv+=value;
		}
		System.out.println();
		StringBuffer sbBuffer=new StringBuffer();
		for (Map.Entry<String, Integer> entry : keywordsvalue.entrySet()) {
			int value=entry.getValue();
			double dd=value/vv;
			String f1=String.format("%.2f", dd);
			sbBuffer.append(f1+" ");
		}
		return sbBuffer;
	}
	
	
}
