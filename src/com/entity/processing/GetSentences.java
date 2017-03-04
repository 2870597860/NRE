package com.entity.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GetSentences {

	public boolean flag=true;//表格标记
	/**
	 * 获取每个公司年报中实体所在的句子以"biaoge"和"text"分类，值为ent+"||"+type+"||"+sectionSentence
	 * @param entityMap HashMap的键是公司拟年报名称，key是以“、”隔开的实体名和实体类别的字符串
	 */
	//获取每个实体所对应的句子或者段落
	public HashMap<String, HashMap<String, List<String>>> getEntitySentence(HashMap<String, TreeSet<String>> entityMap){
		HashMap<String, HashMap<String, List<String>>> entitySentenceMap=new HashMap<>();
		RWText rw=new RWText();
		//存放公司及对应的实体句子
		Set<String> companys=entityMap.keySet();//获取公司名称集合
		for (String company : companys) {
			HashMap<String, List<String>> sentence=new HashMap<>();
			List<String> biaogeSentenceList=new ArrayList<>();//表格中实体所在句子的集合
			List<String> textSentenceList=new ArrayList<>();//文本中实体所在句子的集合
			String companyReportContent=rw.readText(company);//读取每个公司对应的年报文章
			TreeSet<String> entity=entityMap.get(company);
			Iterator<String> str=entity.iterator();	
			while (str.hasNext()) {
				String str2 = (String) str.next();
				//System.out.println("str2:"+str2);
				String[] arr=str2.split("、");
				String ent=arr[0];//实体
				String type=arr[1];//实体类别
				String sectionSentence=findSentence(companyReportContent,ent);//找出实体所在的段或句子
				if (sectionSentence!=null) {
					if (flag) {
						biaogeSentenceList.add(ent+"~"+type+"~"+sectionSentence);
					}else {
						textSentenceList.add(ent+"~"+type+"~"+sectionSentence);
					}
				}
			}
			sentence.put("biaoge", biaogeSentenceList);
			sentence.put("text", textSentenceList);
			entitySentenceMap.put(company, sentence);
		}
		return entitySentenceMap;
	}
	/**
	 * 
	 * @param text
	 * @param entityName
	 * @return 实体在文本中的所在的句子
	 */
	//找出实体在文中对应的句子(实体不在表格中)
	public  String findTextSetence(String text,String entityName){
		String[] str={"。","！"," "};
		String sentence="";
		if(!entityName.matches("[a-zA-Z]+")) {
			//text=GetEntity.readEntity(file);
			int index=text.indexOf(entityName);			
			if (index>0) {
				int startIndex=index;
				TreeSet<Integer> tre=new TreeSet<>();
				for (int i = 0; i < str.length; i++) {
					int temp=text.lastIndexOf(str[i],index);
					if (temp>0) {
						tre.add(temp);
						break;
					}
				}
				for (Integer integer : tre) {
					startIndex=integer;
				}
				int endIndex=text.indexOf("。",index);
				sentence=text.substring(startIndex+1, endIndex+1);
				/*if (entityName.equals("徽商银行")) {
					System.out.println(sentence);
				}*/
				
			}
		}
		return sentence;
	}
	/**
	 * 
	 * @param reportText 公司年报文章
	 * @param entity 实体名
	 * @return 实体在表格中的所在的句子
	 */
	//找出表格实体所在的段或句子(实体在表格中)
	public String findBiaogeSentence(String reportText,String entity){
		//有缺陷需要改善
		int end=reportText.lastIndexOf("序", reportText.indexOf(entity));
		int start1=reportText.lastIndexOf("不适用", reportText.indexOf(entity));
		int start2=reportText.lastIndexOf("研发投入", reportText.indexOf(entity));
		int start3=reportText.lastIndexOf("公司名称", reportText.indexOf(entity));
		String bufenSentence="";
		if (end>0) {
			if (start1>0) {
				if (start2>start1 && end>start2) {
					bufenSentence=reportText.substring(start2,end);
				}else if (end>start1) {
					/*System.out.println(entity);
					System.out.println(start1+";"+end+";"+start2);*/
					int start11=reportText.lastIndexOf("。", reportText.indexOf(entity));
					if (start11>start1) {
						bufenSentence=reportText.substring(start11+"。".length(), end);
					}else {
						bufenSentence=reportText.substring(start1+"不适用".length(), end);
					}
				}else if (start3>start1) {
					bufenSentence=reportText.substring(start1+"不适用".length(), start3);
				}
			}else {
				start1=reportText.lastIndexOf("。", reportText.indexOf(entity));
				bufenSentence=reportText.substring(start1+"。".length(), end);
			}
			
			/*if (start1>0 && start2>0 && start2>start1) {
				if (end>start2) {
					
				}
				
			}else if(end>start1){
				
				bufenSentence=reportText.substring(start1+"不适用".length(), end);
			}*/
		}
		
		return bufenSentence;
	}
	/**
	 * 
	 * @param text 公司年报文章
	 * @param entityName 实体名
	 * @return 返回是表格的句子还是文本的句子（true是表格，false是文本）
	 */
	public String findSentence(String text,String entityName){
		int index=text.indexOf(entityName);//目的是找出实体所在的段落，然后计算相关特征
		String sectionSentence=null;
		if (index>-1) {
			if((text.substring(index+entityName.length(), index+entityName.length()+1).equals(" "))&&(text.substring(index-1, index).equals(" "))){
				flag=true;//是表格
			}else {
				flag=false;//说明不是表格是文本
			}
			if (flag) {//表格处理
				sectionSentence=findBiaogeSentence(text,entityName);//找出表格实体所在的段或句子
			}else {//对实体所在文本进行分析
				sectionSentence=findTextSetence(text,entityName);
			}
			return sectionSentence;
		}
		return null;
	}
}