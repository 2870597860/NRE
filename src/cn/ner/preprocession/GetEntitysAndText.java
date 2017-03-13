package cn.ner.preprocession;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
/**
 * 获取所有公司年报文本，以及年报中的实体和实体类型
 * cn_allCompanyEntityType <公司，<实体，实体类型>>
 * cn_allCompanyText <公司，年报文本>
 * @author Administrator
 *
 */
public class GetEntitysAndText {
	private HashMap<String, TreeMap<String,String>> cn_allCompanyEntityType=new HashMap<>();
	private HashMap<String, String> cn_allCompanyText=new HashMap<>();
	public HashMap<String, TreeMap<String, String>> getCn_allCompanyEntityType() {
		return cn_allCompanyEntityType;
	}
	public HashMap<String, String> getCn_allCompanyText() {
		return cn_allCompanyText;
	}
	//输入所有公司集合，获取每个公司对应的文本以及实体和类型
	public void getTETMain(){	
		TreeSet<String> allCompany=new GetCompanyName().cn_getAllCompany();
		GetPCOEntity rte=new GetPCOEntity();
		for (String everyCompany : allCompany) {
			String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\"+everyCompany+".txt";
			String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity\\entity_"+everyCompany+".txt";
			TreeMap<String,String> Entity_type=getCEType(textPath,entityPath,rte);
			
			cn_allCompanyEntityType.put(everyCompany, Entity_type);
			String text=rte.readText(textPath);//读取文本
			cn_allCompanyText.put(everyCompany, text);
		}
	}
	private TreeMap<String,String> getCEType(String textPath,String entityPath,
			GetPCOEntity rte){
		String entityContent=rte.readText(entityPath);
		TreeSet<String> treeSetEntityArr=rte.getFilter(entityContent);//只获取公司实体、产品实体、组织实体
		TreeMap<String,String> Entity_type=new TreeMap<>();
		for (String string : treeSetEntityArr) {
			String[] entityArr=string.split("、");
			if ( entityArr[0].matches("[a-zA-Z]+") && entityArr[0].length()>5) {
				Entity_type.put(entityArr[0], entityArr[1]);
			}else if ( entityArr[0].matches("[\u4E00-\u9FA5]+") && entityArr[0].length()>=3) {
				Entity_type.put(entityArr[0], entityArr[1]);
				
			}else if (entityArr[0].length()>5) {
				Entity_type.put(entityArr[0], entityArr[1]);
			}
		}
		return Entity_type;
	}

}
