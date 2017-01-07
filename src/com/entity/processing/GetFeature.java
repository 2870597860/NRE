package com.entity.processing;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GetFeature {
	public String similarityCompute(ArrayList<String> keywords,TreeMap<Double, String> similar){
		Set<String> key=entityRelation.keySet();
		//对每一组进行相似度计算
		for (String string : key) {
			String[] feature=entityRelation.get(string);
			//将关键词组成的特征向量和每一组预定的标准特征进行相似度计算，获取坐高的
			String[] arr=new String[keywords.size()];
			arr=keywords.toArray(arr);
			Similarity similarity = new Similarity(feature, arr);//特征向量计算
			double everySim=similarity.sim();
			//将计算的相似度以及对应的归属类别放到TreeMap中（自动排序降序）
			similar.put(everySim,string);

		}
		//获取最大值的特征相似度的并返回标准特征归属
		Set<Map.Entry<Double, String>> entryset=similar.entrySet();
		String maxSimilarFeature="";
		Double maxSimilarValue=0.0;
		for (Map.Entry<Double, String> entry : entryset) {
			/*System.out.println(entry.getValue());
			System.out.println(entry.getKey());*/
			//由相似性计算业务联系以及业务流向
			maxSimilarValue=entry.getKey();
			maxSimilarFeature=entry.getValue();
			break;//仅仅获取第一个值即最大值
		}
		return maxSimilarFeature+"、"+maxSimilarValue;
	}
}
