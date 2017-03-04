package com.entity.processing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthScrollPaneUI;

//定义比较器降序排列
/*class MycomplaPattern implements Comparator<String>{
	TreeMap< String,Double> similar;
	public MycomplaPattern(TreeMap< String,Double> similar) {
		// TODO Auto-generated constructor stub
		this.similar=similar;
	}
	@Override
	public int compare(String s1, String s2) {
		// TODO Auto-generated method stub
		int num=similar.get(s2).compareTo(similar.get(s1));
		return num;
	}

}*/
//关系模式之间的相似度衡量
public class PatternMatching {
	private static double CLIENT=0.40;//阈值
	private static double SUPPORT=0.40;//阈值
	private static double DEVELOP=0.28;//阈值
	private static double ControlSub=0.35;//阈值
	private static StringBuilder sb=new StringBuilder();
	/**
	 * 读取种子模式和经过处理的语料（已经经过处理的关系表达式<实体，实体类别，实体所在句子特征>）
	 */
	public void seedPattternAndSentenceMap(HashMap<String, List<String>> relationPattern){
		List<String> seedList=readSeedPattern();
		getRelationPattern(relationPattern, seedList);
		System.out.println(seedList);
	}
	//对于语料库进行匹配以及关系实例抽取并加入新的关系模型（对关系模式的评价未做）
	//relationPattern的key是公司名称，键是对句子分析的结果是词的形式存储
	public void getRelationPattern(HashMap<String, List<String>> relationPattern,
			List<String> seedList){
		Set<String> companys=relationPattern.keySet();
		//使用自定义的比较器，按照降序排序，以便获取第一个可以和value
		TreeMap<Double,String> similar=new TreeMap<>(new Mycompla());
/*		*//**
		 * 测试
		 *//*
		for (String string : companys) {
			StringBuilder sbb=new StringBuilder();
			List<String> corpusPattern=relationPattern.get(string);
			sbb.append(string);
			sbb.append(corpusPattern.toString());
			addSeedPattern(sbb.toString());
		}*/
		for (String company : companys) {
			sb.append(company+"\n");
			List<String> corpusPattern=relationPattern.get(company);
			Iterator<String> itCorpus=corpusPattern.iterator();
			int en=0;
			while (itCorpus.hasNext()) {
				++en;
				System.out.println("语料中的第"+en+"个实体");
				String ic = (String) itCorpus.next();
				//icArr[0]是实体，icArr[1]实体类型，icArr[2]为可以表达实体关系的特征词汇
				String[] icArr=ic.split("~");
				Iterator<String> itSeed=seedList.iterator();
				List<String> relationSet=new ArrayList<>();
				String[] copyisArr={};
				while (itSeed.hasNext()) {
					String is = (String) itSeed.next();
					//isArr数组大小为3，isArr[0]为实体关系，isArr[1]为实体类别，isArr[2]为可以表达实体关系的特征词汇
					String[] isArr=is.split(",");
					relationSet.add(isArr[0]);
					if (isArr[1].contains("、")) {
						String[] isArrTypeEntity=isArr[1].split("、");
						for (int i = 0; i < isArrTypeEntity.length; i++) {
							patternSimCom(icArr, isArr, isArrTypeEntity[i],similar);
						}
					}else {
						patternSimCom(icArr, isArr, isArr[1],similar);
					}
					copyisArr=isArr;
				}
				//获取匹配最大值并进行阈值过滤
				simMaxAndFilterThreshold(similar,relationSet,icArr,copyisArr,seedList);
				similar.clear();
			}
			System.out.println("======添加关系模板后种子实例集：");
			Iterator<String> itSeed=seedList.iterator();
			while (itSeed.hasNext()) {
				String string = (String) itSeed.next();
				System.out.println(string);
			}
			
		}
		addSeedPattern(sb.toString());
		sb.setLength(0);
	}
	/**
	 * 对语料中的每个实体进行关系归类，并且添加到关系模式中
	 * @param similar 余弦相似度计算结果（关系特征）
	 * @param relationSet 存放种子中的关系，以便语料进行比较
	 * @param icArr 从训练语料中获取的icArr[0]是实体，icArr[1]实体类型，icArr[2]为可以表达实体关系的特征词汇
	 * @param seedList 种子实例
	 */
	public void simMaxAndFilterThreshold(TreeMap<Double, String> similar,List<String> relationSet,
			String[] icArr,String[] isArr,List<String> seedList){
		System.out.println(similar);
		//获取最大值的特征相似度的并返回标准特征归属
		Set<Map.Entry<Double, String>> entryset=similar.entrySet();
		String relationType="";//
		Double maxSimilarValue=0.0;//
		for (Map.Entry<Double, String> entry : entryset) {
			/*System.out.println(entry.getValue());
					System.out.println(entry.getKey());*/
			//由相似性计算业务联系以及业务流向
			maxSimilarValue=entry.getKey();
			relationType=entry.getValue();
			break;//仅仅获取第一个值即最大值
		}
		for (String  relation: relationSet) {
			if (relationType.equals(relation)) {
				if (relationType.equals("客户关系") && (maxSimilarValue>=CLIENT)) {
					addPatternToSet(maxSimilarValue, relationType, relation, icArr, isArr, seedList);
					break;
				}
				if (relationType.equals("供应商关系")&& (maxSimilarValue>=SUPPORT)) {
					System.out.println(maxSimilarValue+"==="+relationType);
					addPatternToSet(maxSimilarValue, relationType, relation, icArr, isArr, seedList);					
					break;
				}
				if (relationType.equals("技术研发")&& (maxSimilarValue>=DEVELOP)) {
					addPatternToSet(maxSimilarValue, relationType, relation, icArr, isArr, seedList);
					break;
				}
				if (relationType.equals("控股子公司")&& (maxSimilarValue>=ControlSub)) {
					addPatternToSet(maxSimilarValue, relationType, relation, icArr, isArr, seedList);
					break;
				}
			}

		}
		//????????????????????????????????????????????

	}
	//添加关系模板到种子实例中
	public void addPatternToSet(Double maxSimilarValue,String relationType,String relation,
			String[] icArr,String[] isArr,List<String> seedList){
		String newPattern=null;
		System.out.println(maxSimilarValue+"==="+relationType);
		String feature=icArr[2].replace("[","").replace("]", "").replaceAll(",", "、").replaceAll(" ", "");
		String newPatternWrite="<"+relation+","+icArr[1]+","+feature+","+icArr[0]+">";
		newPattern=relation+","+icArr[1]+","+feature+","+icArr[0];
		String seed=isArr[2].replaceAll(" ", "");
		if (!feature.equals(seed)) {
			seedList.add(newPattern);//将抽取模板添加到样本模板中
			//addSeedPattern(newPatternWrite);
		}	
		sb.append(newPatternWrite);
		sb.append("\n");
	}
	//模式相似度计算
	public void patternSimCom(String[] icArr,String[] isArr,String isEntityType,
			TreeMap<Double,String> similar){
		if (icArr[1].equals(isEntityType)) {
			/*相似度比较*/
			//arrayList.toString变为字符串之后前后额外加有[],所以要去掉
			String[] icArrFeature=icArr[2].replace("[", "").replace("]", "").replaceAll(" ", "").split(",");
			String[] isArrFeature=isArr[2].replaceAll(" ", "").split("、");
			Similarity similarity = new Similarity(isArrFeature, icArrFeature);//特征向量计算
			double everySim=similarity.sim();
			//将计算的相似度以及对应的归属类别放到TreeMap中（自动排序降序）
			similar.put(everySim,isArr[0]);
		}
	}
	//读取种子模式集合
	public List<String> readSeedPattern(){
		String path="seedPattern/seedRelationInstanceSet.txt";
		List<String> seedList=new ArrayList<>();
		File file=new File(path);
		try {
			FileInputStream in=new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line =null;
			while ((line=br.readLine()) != null) {
				seedList.add(line.replace("<", "").replace(">", ""));
			}
			br.close();
			isr.close();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seedList;
	}
	//添加关系模式
	public void addSeedPattern(String str){
		String path="seedPattern/addRelationInstanceSet.txt";
		try {
			OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(path), "utf-8");
			System.out.println("==================================================");
			System.out.println(str);
			//out.write(company+"::"+"\n");
			out.write(str+"\n");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
