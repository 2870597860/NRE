import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.entity.processing.GetEntityMain;

public class henanhanwei {

	private static String readText(String filePath){		
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
	public static void main(String[] args) {
		String path="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\临时\\河南汉威电子股份有限公司.txt";
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity\\entity_河南汉威电子股份有限公司.txt";
		String text=readText(path);
		System.out.println(text);
		String entityContent=readText(entityPath);
		TreeSet<String> treeSetEntityArr=GetEntityMain.getFilter(entityContent);
		TreeSet<String> treeSetE=new TreeSet<>();
		System.out.println(treeSetEntityArr);
		String arrEntiyquan="";
		for (String string : treeSetEntityArr) {
			String[] entityArr=string.split("、");
			if (entityArr[0].length()>3) {
				treeSetE.add(entityArr[0]);
			}
		}
		HashMap<String, String> mapEntity=new HashMap<>();
		for (String string1 : treeSetE) {			
			for (String string2 : treeSetE) {
				if (string1.contains(string2)&&string1.length()>string2.length()) {
					mapEntity.put(string1, string2);
				}
			}
		}
		System.out.println(mapEntity);
		int indexStart=text.indexOf("主要控股参股公司情况说明");
		int indexEnd=text.indexOf("公司未来发展的展望");
		for (String string : treeSetE) {//treeSetE为去除过短的后的实体集合
			int indexEntity=text.indexOf(string,indexStart);
			if (indexStart>0) {
				if (indexStart<indexEntity && indexEntity<indexEnd) {
					String entirySentence=findTextSetence(text, string,indexEntity);
					//System.out.println(entirySentence+"=====================");
					Set<String> set=mapEntity.keySet();
					//将公司简称换成全称，并将连续的两个连续公司名删除一个
					for (String quancheng : set) {
						String jiancheng=mapEntity.get(quancheng);
						if (entirySentence.contains(quancheng)) {
							//int string3Index=entirySentence.indexOf(quancheng);
							String cache=entirySentence.replace(quancheng, "~");
							cache=cache.replace(jiancheng,quancheng).replace("~", quancheng);
							cache=cache.replace(quancheng+quancheng, quancheng);
							System.out.println(cache);
						}
					}
				}
			}
		}
		
	}
	public static String findTextSetence(String text,String entityName,int index){
		//String[] str={"。","！"," "};
		String sentence="";
		if(!entityName.matches("[a-zA-Z]+")&& entityName.length()>3) {
			if (index>0) {
				int startIndex=index;
				int endIndex=text.indexOf("。",index);
				sentence=text.substring(startIndex, endIndex+1);
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(sentence);
				sentence = m.replaceAll("");
				//System.out.println(sentence);
			}
		}
		return sentence;
	}
}
