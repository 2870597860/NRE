package com.RE.pretreatment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RE.ReadText.ReadReportText;
import com.RE.bosonNLP.BosonNLPAPI;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import edu.fudan.nlp.corpus.StopWords;

public class CWSTagger {
	/**
	 * 
	 * @return 返回每个公司对应的年报分词结果
	 */
	public HashMap<String, String> CWSTaggerText(){
		//获取每个公司对应的年报文本
		HashMap<String, String> reportTextMap=getReportTextAll();
		HashMap<String, String> CWSTaggerResult=new HashMap<>();//每个公司以及对应的分词结果
		final String url="http://api.bosonnlp.com/tag/analysis";
		Set<String> companyReport=reportTextMap.keySet();
		for (String company : companyReport) {
			System.out.println(company);
			String reportText=reportTextMap.get(company);
			System.out.println(reportText);
			String[] reportTextArr=reportText.split("\\*\\*");
			List<String> CWSWords=new ArrayList<>();
			try {
				JsonNode jn=BosonNLPAPI.BosonNLP_RE(url, reportTextArr);
				JSONArray jsonArray=jn.getArray(); 
				for (Object object : jsonArray) {
					JSONObject jo=(JSONObject) object;
					String[] wordArr=getWordList(jo);
					for (int i = 0; i < wordArr.length; i++) {
						CWSWords.add(wordArr[i]);
					}
				}
				if (CWSWords!=null) {
					/*
					 * 这一部分使用停用词进行过滤，去除分词结果中的噪声
					 */
					//获取停用词
					StopWords stopWords = new StopWords("./models/stopwords/StopWords.txt");
				    int size=CWSWords.size();
					// 对分词的结果去除停用词  
				    List<String> baseWords02 = stopWords.phraseDel((String[])CWSWords.toArray(new String[size]));
					CWSTaggerResult.put(company, baseWords02.toString());
				}
			} catch (JSONException | UnirestException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CWSTaggerResult;
	}
	/**
	 * 获取年报文本
	 * @return 公司及其对应的年报文本
	 */
	public HashMap<String, String> getReportTextAll(){
		//文件
		String dirPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing";//读取文件路径
		ReadReportText rrt=new ReadReportText(dirPath);
		return rrt.readReport();
	}
	/**
	 * 获取使用bosonNLP的分词结果
	 * @param jo 为JSONObject，可通过jo获取每个标签对应的值（“word”）
	 * @return
	 */
	public String[] getWordList(JSONObject jo){
		String wordStr=jo.get("word").toString();
		wordStr=wordStr.substring(1, wordStr.length()-1);//去掉头尾的‘[’和‘]’
		String[] wordArr=wordStr.split("\",\"");//将字符串分割成数组
		return wordArr;
	}
	//test
	public static void main(String[] args) {
		CWSTagger cws=new CWSTagger();
		HashMap<String, String> result=cws.CWSTaggerText();
	}
}
