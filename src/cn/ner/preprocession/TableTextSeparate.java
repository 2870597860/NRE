package cn.ner.preprocession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.BufferedHttpEntity;
import org.python.antlr.PythonParser.else_clause_return;

import com.entity.processing.RWText;
/**
 *  获取每个公司年报中实体所在的句子以"biaoge"和"text"分类，值为ent+"||"+type+"||"+sectionSentence
 * @author Administrator
 *
 */
public class TableTextSeparate {
	private boolean flag=true;//表格标记
	private HashMap<String, TreeMap<String,String>> allCompEnType;
	private HashMap<String, String> allCompText;
	//注入所有company文本以及实体和实体类型
	public void setAllCompEnType(HashMap<String, TreeMap<String, String>> allCompEnType) {
		this.allCompEnType = allCompEnType;
	}

	public void setAllCompText(HashMap<String, String> allCompText) {
		this.allCompText = allCompText;
	}
	/**
	 * 获取每个公司年报中实体所在的句子以"biaoge"和"text"分类，值为ent+"||"+type+"||"+sectionSentence
	 * @param entityMap HashMap的键是公司年报名称，key是以“、”隔开的实体名和实体类别的字符串
	 */
	//获取每个实体所对应的句子或者段落
	public HashMap<String, HashMap<String, List<String>>> getEntitySentence(){
		/*
		 * 将每一个公司，表格中的实体和句子中的实体分别存储HashMap<String, List<String>分别为
		 * biaoge和文本的句子
		 */
		HashMap<String, HashMap<String, List<String>>> entitySentenceMap=new HashMap<>();
		Set<String> companys=allCompEnType.keySet();//获取公司名称集合
		for (String company : companys) {
			HashMap<String, List<String>> sentence=new HashMap<>();
			TreeMap<String,String> entityAndTypes=allCompEnType.get(company);
			List<String> biaogeSentenceList=new ArrayList<>();//表格中实体所在句子的集合
			List<String> textSentenceList=new ArrayList<>();//文本中实体所在句子的集合
			Set<String> entitys=entityAndTypes.keySet();
			String companyText=allCompText.get(company); 
			//System.out.println(companyText);
			int indexTen=companyText.indexOf("十、");//过滤最后的调研部分
			if (indexTen>0) {
				companyText=companyText.substring(0, indexTen);
				//System.out.println(companyText);
			}
			for (String entity : entitys) {
				if (entity.equals(company)) {
					continue;
				}
				String entitySentence;
				int indexEntity=companyText.indexOf(entity);//目的是找出实体所在的段落，然后计算相关特征			
				while (indexEntity>0) {
					entitySentence=findSentence(companyText,entity,indexEntity);//得到实体所在的句子
					if (entitySentence!=null ) {
						if (flag) {
							biaogeSentenceList.add(entity+"~"+entitySentence);
						}else {
							textSentenceList.add(entity+"~"+entitySentence);
						}
					}
					indexEntity=companyText.indexOf(entity,indexEntity+entity.length());
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
	 * @param text 公司年报文章
	 * @param entityName 实体名
	 * @return 返回是表格的句子还是文本的句子（true是表格，false是文本）
	 */
	private String findSentence(String text,String entityName,int indexEntity){

		String sectionSentence=null;
		if (indexEntity>-1) {
			if((text.substring(indexEntity+entityName.length(), indexEntity+entityName.length()+1).equals(" "))&&(text.substring(indexEntity-1, indexEntity).equals(" "))){
				flag=true;//是表格
			}else {
				flag=false;//说明不是表格是文本
			}
			if (flag) {//表格处理
				if (entityName.contains("得道")) {
					System.out.println();
				}
				sectionSentence=findBiaogeSentence(text,entityName,indexEntity);//找出表格实体所在的段或句子
			}else {//对实体所在文本进行分析
				sectionSentence=findTextSetence(text,entityName,indexEntity);
			}
		}
		return sectionSentence;
	}
	/**
	 * 
	 * @param reportText 公司年报文章
	 * @param entity 实体名
	 * @return 实体在表格中的所在的句子
	 */
	//找出表格实体所在的段或句子(实体在表格中)
	private String findBiaogeSentence(String reportText,String entity,int indexEntity){
		//有缺陷需要改善
		if (entity.equals("浙江物产电子商务有限公司")) {
			System.out.println();
		}
		int xu=reportText.lastIndexOf("序", indexEntity);
		int juhao=reportText.lastIndexOf("。", indexEntity);
		int bushiyong=reportText.lastIndexOf("不适用", indexEntity);
		int yanfa=reportText.lastIndexOf("研发投入", indexEntity);
		int companyName=reportText.lastIndexOf("公司名称", indexEntity);
		int companyName1=reportText.lastIndexOf("企业名称", indexEntity);
		String bufenSentence=null;
		if (entity.equals("广东龙泉科技有限公司")) {
			System.out.println();
		}
		/*if (xu>0) {
			if (yanfa>0 && xu>yanfa ) {
				bufenSentence=reportText.substring(yanfa,xu);
			}
			if (bushiyong>0) {
				if (bushiyong>juhao && xu>bushiyong) {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), xu);
				}else if (juhao>0 && xu>juhao) {
					bufenSentence=reportText.substring(juhao+"。".length(), xu);
				}
				if (companyName>bushiyong || companyName1>bushiyong ) {
					if (companyName>bushiyong) {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), companyName);
					}
					if (companyName1>bushiyong) {
						bufenSentence=reportText.substring(bushiyong, companyName1);
					}				
					if (bufenSentence.getBytes().length==bufenSentence.length()) {
						bufenSentence="主要子公司及对公司净利润影响达 10%以上的参股公司情况";//默认值
					}
				}
			}
		}*/

		if (xu>0) {
			if (bushiyong>0) {
				if (yanfa>bushiyong && xu>yanfa) {
					bufenSentence=reportText.substring(yanfa,xu);
				}else if (xu>bushiyong) {
					if (juhao>bushiyong) {
						if (xu>juhao+"。".length()) {
							bufenSentence=reportText.substring(juhao+"。".length(), xu);
						}
					}else {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), xu);
						if (bufenSentence.getBytes().length==bufenSentence.length()||bufenSentence.length()<6) {
							bufenSentence="研发投入,报告期公司已经进入注册程序的研发产品进展情况如下";//默认值
						}
					}
				}else if (companyName>bushiyong || companyName1>bushiyong ) {
					if (companyName>bushiyong) {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), companyName);
					}
					if (companyName1>bushiyong) {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), companyName1);
					}				
					if (bufenSentence.getBytes().length==bufenSentence.length()) {
						bufenSentence="主要子公司及对公司净利润影响达 10%以上的参股公司情况";//默认值
					}
				}
			}else{
				int kuohao=reportText.lastIndexOf("）", xu);
				if (kuohao>0) {
					bufenSentence=reportText.substring(kuohao+"）".length(), xu);
				}
			}
		}
		return bufenSentence;
	}
	/**
	 * 
	 * @param text
	 * @param entityName
	 * @return 实体在文本中的所在的句子
	 */
	//找出实体在文中对应的句子(实体不在表格中)
	private  String findTextSetence(String text,String entityName,int indexEntity){
		//String[] str={"。","！"," "};
		String sentence=null;
		int index1=text.lastIndexOf("重大资产和股权出售",indexEntity);
		int index2=text.lastIndexOf("主要控股参股公司分析",indexEntity);
		int index3=text.lastIndexOf("报告期内取得和处置子公司的情况",indexEntity);
		/*	if (entityName.contains("富维延锋彼欧汽车外饰有限公司")) {
			System.out.println();
		}*/
		if(!entityName.matches("[a-zA-Z]+")&& entityName.length()>3) {
			if (indexEntity>0) {
				if ((index1>0 &&indexEntity>index1 || (index2>0 &&indexEntity>index2)||(index3>0 &&indexEntity>index3)) ) {
					int startIndex=indexEntity;
					int endIndex=text.indexOf("。",indexEntity);

					sentence=text.substring(startIndex, endIndex+"。".length());
					/*if (sentence.contains("福安药业集团宁波天衡制药有限公司")) {
						System.out.println(sentence);
					}*/
					Pattern p = Pattern.compile("\t|\r|\n");
					Matcher m = p.matcher(sentence);
					sentence = m.replaceAll("").replaceAll(" +"," ");//将多个空格替换为一个空格必须用replaceAll
					//System.out.println(sentence);
				}
			}
		}
		return sentence;
	}
}
