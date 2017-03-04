package com.RE.dp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindTextSetence {
	public  String findTextSetence(String text,String entityName,int index){
		//String[] str={"。","！"," "};
		String sentence="";
		if(!entityName.matches("[a-zA-Z]+")&& entityName.length()>3) {
			if (index>0) {
				int startIndex=index;
				int endIndex=text.indexOf("。",index);

				sentence=text.substring(startIndex, endIndex+"。".length());
				/*if (sentence.contains("福安药业集团宁波天衡制药有限公司")) {
					System.out.println(sentence);
				}*/
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(sentence);
				sentence = m.replaceAll("");
				//System.out.println(sentence);
			}
		}
		return sentence;
	}
}
