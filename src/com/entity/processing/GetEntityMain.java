package com.entity.processing;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import com.txt.processing.ReadFiles;
import com.txt.processing.WriteContent;

/**
 * 读取路径文本中的entity(已经经过处理的文本得到的entity，存入文本中)，并作一定的处理获取自己想要的entity
 * @author Administrator
 *
 */
public class GetEntityMain {
	public static void main(String[] args) {
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		HashMap<String, TreeSet<String>> entityMap=new HashMap<>();
		WriteContent wrt=new WriteContent();
		try {
			List<String> fileLists=ReadFiles.readDirs(entityPath);//返回文件的绝对路径
			for (String file : fileLists) {
				String entityContent=null;
				String Company=null;
				TreeSet<String> treeSet=null;
				int preCom=file.indexOf("entity_");
				int laterCom=file.indexOf(".txt");
				Company=file.substring(preCom+"entity_".length(), laterCom);
				//读取文件内容
				entityContent= readEntity(file);
				//对实体进行过滤
				treeSet=getFilter(entityContent);
				entityMap.put(Company, treeSet);//将公司以及公司年报对应的实体存储起来
				System.out.println(treeSet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//建立特征向量
		BuildVector bv=new BuildVector(); 
		bv.extractFeature(entityMap);
		System.out.println("处理完毕：");
	}
	public static String readEntity(String filePath){
		StringBuffer sb = new StringBuffer();
		InputStreamReader is;
		try {
			is = new InputStreamReader(new FileInputStream(filePath), "utf-8");
			BufferedReader br = new BufferedReader(is);
			String line =null;
			while ((line=br.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n");
			}
			br.close(); 
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();

	} 
	/**
	 * 因为要对识别的实体进行过滤，一个公司年报中所有的实体以及类型
	 * product_name、company_name的实体
	 */
	
	public static TreeSet<String> getFilter(String content){
		TreeSet<String> set=new TreeSet<>();
		StringBuilder sb=new StringBuilder();
		String[] entity=content.split("\r\n");
		for (String string : entity) {
			if (string.contains("product_name")||string.contains("company_name")) {
				if (!string.contains(".")) {
					String str=string.replaceAll("\\d+", "");//去掉字符串子所有的数字
					if(str.contains("product_name")){
						sb.append(str);
						sb.insert(sb.indexOf("p"), "、");
					}
					if(str.contains("company_name")){
						sb.append(str);
						sb.insert(sb.indexOf("c"), "、");
					}
					set.add(sb.toString());
					sb.delete(0, sb.length());
				}
			}
		}
		return set;
	}
}
