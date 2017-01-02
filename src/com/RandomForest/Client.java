package com.RandomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 随机森林算法测试场景
 * 
 * @author lyq
 * 
 */
public class Client {
	public static void main(String[] args) {
		//训练样本路径
		String filePath = "./model.txt";
		//读取测试样本信息
		String testDataPath="./test.txt";
		ArrayList<String> dataArray = new ArrayList<String>();
		String resultClassType = "";
		// 决策树的样本占总数的占比率
		double sampleNumRatio = 1;
		// 样本数据的采集特征数量占总特征的比例
		double featureNumRatio = 0.6;
		RandomForestTool tool = new RandomForestTool(filePath, sampleNumRatio,
				featureNumRatio);
		// 构造随机森林
		tool.constructRandomTree();
		//用来存储分类为是的个数
		//int count=0;
		String queryStr="";
		readTestData(testDataPath,dataArray);
		for (String strings : dataArray) {
			queryStr=strings;
			//String queryStr = "Age=Youth,Income=Low,Student=No,CreditRating=Fair";
			resultClassType = tool.judgeClassType(queryStr);

			System.out.println();
			System.out
			.println(MessageFormat.format(
					"查询属性描述{0},预测的分类结果为BuysCompute:{1}", queryStr,
					resultClassType));
/*			if (resultClassType.equals("是")) {
				count++;
				
			}
			System.out.println("分类为是的个数："+count);*/
		}
		
	}
	public static void readTestData(String testFilePath,ArrayList<String> dataArray){
		File file = new File(testFilePath);
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split("\n");
				dataArray.add(tempArray[0]);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
}