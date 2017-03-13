package cn.ner.preprocession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

public class GetPCOEntity {
	/**
	 * 因为要对识别的实体进行过滤，一个公司年报中所有的实体以及类型
	 * product_name、company_name的实体
	 */
	
	public  TreeSet<String> getFilter(String content){
		TreeSet<String> set=new TreeSet<>();
		StringBuilder sb=new StringBuilder();
		String[] entity=content.split("\r\n");
		for (String string : entity) {
			if (string.contains("product_name")||string.contains("company_name")||string.contains("org_name")) {
				if (!string.contains(".")) {
					fenGe(sb,string);					
				}else if((string.length()>16)){
					fenGe(sb,string);
				}
				set.add(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		return set;
	}
	public void fenGe(StringBuilder sb,String string){
		String str=string.replaceAll("\\d+", "");//去掉字符串子所有的数字
		if(str.contains("product_name")){
			sb.append(str);
			sb.insert(sb.indexOf("p"), "、");
		}
		if(str.contains("company_name")){
			sb.append(str);
			sb.insert(sb.indexOf("c"), "、");
		}
		if (str.contains("org_name")) {
			sb.append(str);
			sb.insert(sb.indexOf("o"), "、");
		}
	}
	public  String readText(String filePath){		
		String content="";
		StringBuffer sb = new StringBuffer();
		File file=new File(filePath);
		try {
			InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"utf-8");
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line+"\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
