package com.RE.bosonNLP;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BosonNLPAPI {
	public static JsonNode BosonNLP_RE(String URL, String[] arr) throws JSONException,UnirestException, IOException{
		
		String body = new JSONArray(arr).toString();
		HttpResponse<JsonNode> jsonResponse = Unirest.post(URL)
				.header("Accept", "application/json")
				.header("X-Token", "CVvo15qJ.10245.DaD8xZLWur4Z")
				.body(body)
				.asJson();
		JsonNode jn=jsonResponse.getBody();
		return jn;
	}
	//test
	public static void main(String[] args) {
		String url="http://api.bosonnlp.com/tag/analysis";
		String[] arr=new String[]{"工作, 实现 工作,实现福安药业集团宁波天衡制药有限公司于2015年4月23日完成对宁波天衡的收购重组工作,实现净利润339.31万元"};
		try {
			JsonNode jn=BosonNLP_RE(url, arr);
			System.out.println(jn);
		} catch (JSONException | UnirestException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
