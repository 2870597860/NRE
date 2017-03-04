import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.transform.stax.StAXSource;

public class GetIndex {
	public static void main(String[] args) {
		String path="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\临时\\河南汉威电子股份有限公司.txt";
		String content=readText(path);
		System.out.println(content);
		int s1=content.indexOf("嘉园环保有限公司");
		int s11=s1+"郑州炜盛电子科技有限公司".length();		
		int ss=content.indexOf("嘉园环保",s1+"嘉园环保有限公司".length());
		int ss1=content.lastIndexOf(" ",ss);
		System.out.println("===");
		System.out.println("嘉园环保有限公司:"+s1+"空格："+ss1+":"+ss);
		String kongge=content.substring(ss-1, ss);
		System.out.println("字符为："+kongge+"dsf");
		System.out.println("===");
		int s2=content.indexOf("郑州炜盛电子科技有限公司",s1);
		int s3=content.indexOf(" ",s1);
		System.out.println(s1+" "+s11+"    "+s2+"  "+s3 );
		String testStr="嘉园环保嘉园环保为本公司嘉园环保有限公司的控股子公司，注册嘉园环保资本6,000万元。";
		StringBuffer ssbb=new StringBuffer();
		ssbb.append(testStr);
		int first=ssbb.indexOf("嘉园环保有限公司");
		int second=ssbb.indexOf("嘉园环保",first+"嘉园环保有限公司".length());
		ssbb.insert(first, "嘉园环保有限公司");
		System.out.println(first+";;;;;;;"+second+ssbb);
		String gegehe="fwefg大囧鼎大厦鼎大厦dfsafas鼎大厦鼎大厦";
		gegehe=gegehe.replace("鼎大厦鼎大厦", "鼎大厦");
		//testStr.replace("郑州炜盛电子科技有限公司", "嘉园环保");
		System.out.println("gegehe:"+gegehe);
		System.out.println(gegehe.matches(".*鼎大厦鼎大厦.*"));
		TreeSet<String> tree=new TreeSet<>();
		tree.add("我是");
		tree.add("我是");
		System.out.println(tree);
		while(true){
			
			for (int i = 0; i < 1000; i++) {
				if (i==500) {
					break;
				}
			}	
			System.out.println("while内部");
			break;
		}
		//System.out.println("while外部");
		TreeMap<Integer, String> trssmap=new TreeMap<>();
		trssmap.put(11, "和慈善毒贩");
		trssmap.put(9, "和慈善毒贩");
		System.out.println(trssmap);
		String st="嘲讽我v问";
		String sst=st;
		System.out.println(sst==st);
		sst=sst+"fdsfs";
		System.out.println(sst==st);
		System.out.println("st:"+st+",sst:"+sst);
		TreeSet<String> s=new TreeSet<>();
		s.add("dd");
		s.add("dfsf");
		StringBuffer abbbb=new StringBuffer();
		abbbb.append(s);
		System.out.println(abbbb.indexOf("[")+","+"[".length());
		abbbb.delete(abbbb.length()-2, abbbb.length()-1);
		System.out.println(abbbb.toString());
	}
	private static String readText(String filePath){		
		String content="";
		File file=new File(filePath);
		try {
			InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"utf-8");
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			while((line=br.readLine())!=null){
				content+=line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}
