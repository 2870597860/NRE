package cn.ner.dpmain2;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;  
import java.net.URL;  
import java.net.URLEncoder;

import org.python.antlr.ast.Slice.Slice___init___exposer;  
public class LTPAPI_dp {
	public static StringBuffer sentenceDP(String text)throws IOException{  
		String api_key = "z8z4W3E6zCgisEkzqf7dAdgYleZrSPbWiVFGXMUy";//api_key,申请账号后生成，这个账户每月有19G流量  
		String pattern = "dp";//ws表示只分词，除此还有pos词性标注、ner命名实体识别、dp依存句法分词、srl语义角色标注、all全部  
		String format  = "xml";//指定结果格式类型，plain表示简洁文本格式  
		String result = "";  
		
		text = URLEncoder.encode(text, "utf-8");  
		URL url = new URL("http://api.ltp-cloud.com/analysis/?"  
				+ "api_key=" + api_key + "&"  
				+ "text="    + text    + "&"  
				+ "format="  + format  + "&"  
				+ "pattern=" + pattern);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.connect();  
		InputStreamReader isr=new InputStreamReader(conn.getInputStream(), "utf-8");
		BufferedReader innet = new BufferedReader(isr);
		StringBuffer sb=new StringBuffer();
		String line;  
		while ((line = innet.readLine())!=null){
			sb.append(line+"\n");
			//System.out.println(line);  
		}  
		//System.out.println("xml:"+sb.toString());
		innet.close();
		return sb;  
	} 
	//语义角色标注
	public static StringBuffer sentenceDPsrl(String text)throws IOException{  
		String api_key = "z8z4W3E6zCgisEkzqf7dAdgYleZrSPbWiVFGXMUy";//api_key,申请账号后生成，这个账户每月有19G流量  
		String pattern = "srl";//ws表示只分词，除此还有pos词性标注、ner命名实体识别、dp依存句法分词、srl语义角色标注、all全部  
		String format  = "xml";//指定结果格式类型，plain表示简洁文本格式  
		String result = "";  
		
		text = URLEncoder.encode(text, "utf-8");  
		URL url = new URL("http://api.ltp-cloud.com/analysis/?"  
				+ "api_key=" + api_key + "&"  
				+ "text="    + text    + "&"  
				+ "format="  + format  + "&"  
				+ "pattern=" + pattern);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.connect();  
		InputStreamReader isr=new InputStreamReader(conn.getInputStream(), "utf-8");
		BufferedReader innet = new BufferedReader(isr);
		StringBuffer sb=new StringBuffer();
		String line;  
		while ((line = innet.readLine())!=null){
			sb.append(line+"\n");
			//System.out.println(line);  
		}  
		System.out.println("xml:"+sb.toString());
		innet.close();
		return sb;  
	} 
	public static void main(String[] args) {
		String text = "报告期内，长安福特，长安马自达销量增加，导致净利润均同比有较大增长。";
		try {
			//sentenceDP(text);
			StringBuffer sbb=sentenceDP(text);
			System.out.println(sbb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(text+"分析失败");
		}
	}
}
