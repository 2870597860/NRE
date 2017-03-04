package com.RE.nameEntityRecoMain;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
/**
 * 命名实体识别
 * @author Administrator
 *
 */
public class NameEntityRe
{	//实体识别url
	public static final String SENTIMENT_URL =
			"http://api.bosonnlp.com/ner/analysis";
	//对文本进行实体识别
	/**
	 * 对文本进行实体识别
	 * @param arr 要识别的文本，以数组的形式展现作为输入
	 * @return 返回key=实体，value=实体对应的类型
	 * @throws JSONException
	 * @throws UnirestException
	 * @throws IOException
	 */
	public static HashMap<String, String>  NameEntityReMain(String[] arr) throws JSONException,UnirestException, IOException{
		
		String body = new JSONArray(arr).toString();
		HttpResponse<JsonNode> jsonResponse = Unirest.post(SENTIMENT_URL)
				.header("Accept", "application/json")
				.header("X-Token", "CVvo15qJ.10245.DaD8xZLWur4Z")
				.body(body)
				.asJson();
		JsonNode jn=jsonResponse.getBody();
		System.out.println("结果："+jn);
		JSONArray jsonArray=jn.getArray();
		NameEntityRe ner=new NameEntityRe();
		HashMap<String, String> entityAndType=new HashMap<>();
		for (Object object : jsonArray) {
			JSONObject jo=(JSONObject) object;
			ner.HandleJsonResponse(jo,entityAndType);
		}
		return entityAndType;
	}
	/**
	 * 
	 * @param jo 键和值，可以通过键获取对应的值
	 * @param entityAndType
	 */
	public void HandleJsonResponse(JSONObject jo,HashMap<String, String> entityAndType){
		List<String> WordList=getWordList(jo);
		System.out.println(WordList);
		List<String> entityArrList=getEntityList(jo);
		if (entityArrList!=null) {
			for (String string : entityArrList) {
				System.out.println(string);
			}
			getEntityAndtype(WordList, entityArrList,entityAndType);
		}	
		System.out.println(entityAndType);
	}
	//获取实体及其对应的实体类型
	public void getEntityAndtype(List<String> WordList,
			List<String> entityArrList,HashMap<String, String> entityAndType){
		if (entityArrList.size()>0 && WordList.size()>0) {
			for (String string : entityArrList) {
				StringBuilder sb=new StringBuilder();
				String[] cache=string.split(",");
				for (int i = Integer.parseInt(cache[0]); i < Integer.parseInt(cache[1]); i++) {
					sb.append(WordList.get(i));
				}
				entityAndType.put(sb.toString().replace("\"", ""), cache[2].replace("\"", ""));
			}
		}
	}
	/*
	 * 获取word键对应的值
	 */
	public List<String> getWordList(JSONObject jo){
		String wordStr=jo.get("word").toString();
		wordStr=wordStr.substring(1, wordStr.length()-1);
		String[] wordArr=wordStr.split("\",\"");
		return Arrays.asList(wordArr);
	}
	/*
	 * 获取实体集合
	 */
	public List<String> getEntityList(JSONObject jo){
		String entityStr=jo.get("entity").toString();
		entityStr=entityStr.substring(1, entityStr.length()-1);
		System.out.println(entityStr);
		String[] entityArr=null;
		if (entityStr.contains("],[")) {
			entityArr=entityStr.split("\\],\\[");
			entityArr[0]=entityArr[0].replace("[", "");
			entityArr[entityArr.length-1]=entityArr[entityArr.length-1].replace("]", "");
			System.out.println("去除找中括号后："+entityStr);

		}else {
			//System.out.println("====="+entityArr);
			if (!entityStr.equals("")) {
				entityArr=entityStr.substring(1, entityStr.length()-1).split("~");
			}else {
				return null;
			}
		}
		return Arrays.asList(entityArr);
	}
	//测试
	public static void main(String[] args)
	{
		String[] arr=new String[]{"工作,实现工作,实现福安药业集团宁波天衡制药有限公司于2015年4月23日完成对宁波天衡的收购重组工作,实现净利润339.31万元"};
		try {
			NameEntityReMain(arr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
