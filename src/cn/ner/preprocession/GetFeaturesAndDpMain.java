package cn.ner.preprocession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.http.nio.reactor.ListeningIOReactor;

import cn.ner.readwrite.WriteContent;



public class GetFeaturesAndDpMain {
	//每个公司对应的实体的特征量化向量
	private HashMap<String, List<String>> lianghuaResult=new HashMap<>();
	
	public HashMap<String, List<String>> getLianghuaResult() {
		return lianghuaResult;
	}
	public void getSentenceMapMain(){
		//HashMap<String, HashMap<String, List<String>>> 
		//获取所有公司年报文本，以及年报中的实体和实体类型
		GetEntitysAndText geat=new GetEntitysAndText();
		geat.getTETMain();
		HashMap<String, TreeMap<String,String>> allCompEnType=geat.getCn_allCompanyEntityType();
		HashMap<String, String> allCompText=geat.getCn_allCompanyText();

		//注入所有company文本以及实体和实体类型
		TableTextSeparate tts=new TableTextSeparate();
		tts.setAllCompEnType(allCompEnType);
		tts.setAllCompText(allCompText);

		GetKeyWords gw=new GetKeyWords();
		
		WriteContent wr=new WriteContent();		
		//获取已经分类好的句子
		HashMap<String, HashMap<String, List<String>>> entitySentenceMap=tts.getEntitySentence();
		Set<String> companys=entitySentenceMap.keySet();
		System.out.println("要处理的公司公司总数为："+entitySentenceMap.size());
		for (String company : companys) {
			HashMap<String, List<String>> separateSentences=entitySentenceMap.get(company);
			Set<String> flags=separateSentences.keySet();
			for (String flag : flags) {
				List<String> sentences=separateSentences.get(flag);
				if (flag.equals("biaoge")) {
					//lianghuaVector中用~将实体和特征向量隔开
					List<String> lianghuaVector=getFeaturesVevtor(sentences,gw);
					lianghuaResult.put(company, lianghuaVector);
					//这是测试数据，写入文本
					StringBuffer cacheVector=new StringBuffer();
					for (String string : lianghuaVector) {
						cacheVector.append(string+"\n");
					}
					wr.writeConAppend(company+":\n"+cacheVector.toString(), "./seedPattern/testfile");
					
				}else {
					String sentencesSb=FilterSentences(allCompEnType, company, sentences);
					wr.writeCon(sentencesSb, "./DPSentence_will/"+company+".txt");
				}
			}
		}
		System.out.println("处理完毕：；；；；；；；");
	}
	private List<String> getFeaturesVevtor(List<String> sentences,GetKeyWords gw){
		int keyWordsNumber=8;
		List<String> keyWords=new ArrayList<>();
		for (String sentence : sentences) {
			try {
				//strArr[0]是实体，strArr[1]是实体所关联的句子
				String[] strArr=sentence.split("~");
				StringBuffer  sbKeywords= gw.GetKeyword(strArr[1], keyWordsNumber);
				keyWords.add(strArr[0]+"~"+sbKeywords.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//返回关键词抽取结果list集合
		}
		return keyWords;
	}
	private String FilterSentences(HashMap<String, TreeMap<String,String>> allCompEnType,String company,
			List<String> sentences){
		TreeMap<String,String> EntityAndType=allCompEnType.get(company);
		HashMap<String, String> mapEntity=new HashMap<>();//<简称,全称>
		TreeSet<String> quChongSentence=new TreeSet<>();
		Set<String> entitys=EntityAndType.keySet();
		for (String entity1 : entitys) {
			//System.out.println("=="+entity1);
			for (String entity2 : entitys) {
				if (entity1.contains(entity2)&& entity1.length()>entity2.length()) {
					mapEntity.put(entity2, entity1);//<简称,全称>
					continue;
				}
			}
			mapEntity.put(entity1,null);
		}	
		for (String sentence : sentences) {
			//arrEntitySentence[0]是实体，arrEntitySentence[1]是句子
			String[] arrEntitySentence=sentence.split("~");	
			String entity=arrEntitySentence[0];
			String entirySentence=arrEntitySentence[1];
			Set<String> set=mapEntity.keySet();
			if (mapEntity.containsKey(entity)) {
				String quanchengEntity=mapEntity.get(entity);
				if (quanchengEntity!=null) {
					if (entirySentence.contains(quanchengEntity)) {
						if (entirySentence.contains("报告期，")) {
							int indexCache=entirySentence.indexOf("报告期");
							entirySentence=entirySentence.substring(indexCache+"报告期".length()+1);
						}	
						if (entirySentence.contains("报告期内，")) {
							int indexCache=entirySentence.indexOf("报告期内");
							entirySentence=entirySentence.substring(indexCache+"报告期内".length()+1);
						}	

						//去除连续相同的实体
						if (entirySentence.contains(quanchengEntity+entity)) {
							entirySentence=entirySentence.replace(quanchengEntity+entity, quanchengEntity);
							break;
						}
						if (entirySentence.contains(entity+quanchengEntity)) {
							entirySentence=entirySentence.replace(entity+quanchengEntity, quanchengEntity);
							break;
						}
					}
				}
			}
			quChongSentence.add(entirySentence);	
		}
		System.out.println(mapEntity);
		StringBuffer sb=new StringBuffer();//存放实体所在的句子
		for (String string : quChongSentence) {
			sb.append(string);
		}
		return sb.toString();
	}
}
