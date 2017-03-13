package cn.ner.main1;

import java.util.HashMap;
import java.util.List;

import cn.ner.hibernate.service.RelationMain;
import cn.ner.knn.KNNClasMain;
import cn.ner.preprocession.GetFeaturesAndDpMain;
/**
 *分三步，第一：获取特征向量（量化的）以及要依存分析的句子
 *第二： knn分类以及种子扩展
 *第三：将分类结果存入数据库
 * @author Administrator
 *
 */
public class StartMain {
	public static void main(String[] args) {
		/*
		 * 获取实体句子的特征以及将要依存分析的句子
		 */
		GetFeaturesAndDpMain gfadp=new GetFeaturesAndDpMain();
		gfadp.getSentenceMapMain();
		//knn分类
		KNNClasMain knnc=new KNNClasMain();
		knnc.getVectorDatas(gfadp);
		//获取分类结果
		HashMap<String, HashMap<String, String>> classifacResults=knnc.getClassifacResults();
		
		//调用数据库，存入数据库
		RelationMain rm=new RelationMain();
		rm.dataOperatorBefore();
		rm.dataOperator(classifacResults);
		rm.dataOperatorAfter();
				
	}
}
