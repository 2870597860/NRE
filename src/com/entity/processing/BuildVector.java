package com.entity.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.txt.processing.ReadFiles;
import com.txt.processing.WriteContent;

import edu.fudan.example.nlp.DepParser;

/**
 * 建立需要的特征向量、、、、、、未被使用
 * @author Administrator
 *
 */
//定义比较器降序排列
class Mycompla implements Comparator<Double>{

	@Override
	public int compare(Double o1, Double o2) {
		// TODO Auto-generated method stub
		int num=o2.compareTo(o1);
		return num;
	}

}
public class BuildVector {
	private static double CLIENT=0.55;//阈值
	private static double SUPPORT=0.55;//阈值
	private static double DEVELOP=0.40;//阈值
	private static double INVEST=0.60;//阈值
	static HashMap<String, String[]> entityRelation=new HashMap<>();
	//提取特征,其中TreeSet<String>中存储的是以“、”隔开的实体名和实体类别的字符串
	public void extractFeature(HashMap<String, TreeSet<String>> map){
		define();
		//存储最终的公司年报中获取的特征向量存储形式为<年报公司名,<年报中实体名称，[实体类别、业务联系、业务流向、资金支持]>>
		HashMap<String, HashMap<String, TreeSet<String>>> featureVector=new HashMap<>();
		Set<String> key=map.keySet();
		for (String company : key) {
			//存放实体以及是实体和公司之间的联系特征<实体,[实体类别、业务联系、业务流向、资金支持]>
			HashMap<String, TreeSet<String>> buildVector=new HashMap<>();
			StringBuilder sb=new StringBuilder();
			String companyReportContent=readText(company);//读取每个公司对应的年报文章
			TreeSet<String> entity=map.get(company);
			Iterator<String> str=entity.iterator();	
			while (str.hasNext()) {
				TreeSet<String> treesb=new TreeSet<>();
				String str2 = (String) str.next();
				String[] arr=str2.split("、");
				String ent=arr[0];//实体
				String type=arr[1];//实体类别
				//获取实体名并查找在文中具体位置，以获取相应特征
				String firstFea=type+"、";
				String flag=findFeature(companyReportContent,arr[0],firstFea,sb);
				if (flag.equals("can")) {
					treesb.add(sb.toString());
					buildVector.put(ent, treesb);
					sb.setLength(0);
				}
			}			
			featureVector.put(company, buildVector);
		}
		String path="./featureVector.txt";
		writeVector(featureVector,path);//将抽取计算好的特征向量写入文件
	}
	public void writeVector(HashMap<String, HashMap<String, TreeSet<String>>> featureVector,String path){

		WriteContent wrt=new WriteContent();
		Set<Map.Entry<String, HashMap<String, TreeSet<String>>>> fea=featureVector.entrySet();
		for (Map.Entry<String, HashMap<String, TreeSet<String>>> entry : fea) {
			HashMap<String, TreeSet<String>> hmValue=entry.getValue();
			String strKey=entry.getKey(); 
			Set<Map.Entry<String, TreeSet<String>>> hmhm=hmValue.entrySet();
			String insidehm="";
			for (Map.Entry<String, TreeSet<String>> entry2 : hmhm) {
				insidehm+=entry2.toString()+"\n";
			}
			wrt.writeCon(strKey+":\n"+insidehm+"\n", path);
		}

	}
	//定义相似度计算的标准对比特征向量
	public static void define(){
		String[] s1 = {"销售","客户","金额"}; 
		String[] s2 = {"供应","供应商","采购"}; 
		String[] s3 = {"研发","技术","药品","投入"}; 
		String[] s4 = {"利润", "分", "红款","分红"}; //资金支持这一项待定
		entityRelation.put("客户",s1);
		entityRelation.put("供应商", s2);
		entityRelation.put("研发", s3);
		entityRelation.put("资金控股", s4);
	}
	public String similarityCompute(ArrayList<String> keywords,TreeMap<Double, String> similar){

		Set<String> key=entityRelation.keySet();
		//对每一组进行相似度计算
		for (String string : key) {
			String[] feature=entityRelation.get(string);
			//将关键词组成的特征向量和每一组预定的标准特征进行相似度计算，获取坐高的
			String[] arr=new String[keywords.size()];
			arr=keywords.toArray(arr);
			Similarity similarity = new Similarity(feature, arr);//特征向量计算
			double everySim=similarity.sim();
			//将计算的相似度以及对应的归属类别放到TreeMap中（自动排序降序）
			similar.put(everySim,string);

		}
		//获取最大值的特征相似度的并返回标准特征归属
		Set<Map.Entry<Double, String>> entryset=similar.entrySet();
		String maxSimilarFeature="";
		Double maxSimilarValue=0.0;
		for (Map.Entry<Double, String> entry : entryset) {
			/*System.out.println(entry.getValue());
			System.out.println(entry.getKey());*/
			//由相似性计算业务联系以及业务流向
			maxSimilarValue=entry.getKey();
			maxSimilarFeature=entry.getValue();
			break;//仅仅获取第一个值即最大值
		}
		return maxSimilarFeature+"、"+maxSimilarValue;
	}
	public String findFeature(String text,String entityName,String firstFea,StringBuilder sb){
		int index=text.indexOf(entityName);//目的是找出实体所在的段落，然后计算相关特征
		boolean flag=true;//表格标记
		String eF="";
		//使用自定义的比较器，按照降序排序，以便获取第一个可以和value
		TreeMap<Double, String> similar=new TreeMap<>(new Mycompla());
		GetKeyWords gw=new GetKeyWords();
		String maxSimilarFeature="";
		if((text.substring(index+entityName.length(), index+entityName.length()+1).equals(" "))&&(text.substring(index-1, index).equals(" "))){
			flag=true;//是表格
		}else {
			flag=false;//说明不是表格是文本
		}
		if (flag) {//表格处理
			int keyWordsNumber=10;
			String sectionSentence=findSectionSentence(text,entityName);//找出表格实体所在的段或句子
			try {

				ArrayList<String> keywords= gw.GetKeyword(sectionSentence, keyWordsNumber);//返回关键词抽取结果list集合
				if (keywords.size()>0) {
					String[] maxSimilar=similarityCompute(keywords,similar).split("、");
					maxSimilarFeature=thresholdFilter(maxSimilar,maxSimilarFeature);//定义阈值只有大于阈值的特征才进行存储
					if (maxSimilarFeature!=null) {
						String resultVector=serviceParse(maxSimilarFeature);
						sb.append(firstFea);
						sb.append(resultVector);
						eF="can";
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else {//对实体所在文本进行分析
			SentenceParse sp=new SentenceParse();
			ArrayList<String> array=sp.depparser(text,entityName);
			//array有值才做处理
			if (array!=null) {
				String[] maxSimilar=similarityCompute(array,similar).split("、");
				maxSimilarFeature=thresholdFilter(maxSimilar,maxSimilarFeature);
				if (maxSimilarFeature!=null) {
					String resultVector=serviceParse(maxSimilarFeature);
					sb.append(firstFea);
					sb.append(resultVector);
					eF="can";
				}
			}
		}
		return eF;

	}
	//定义阈值，只有大于阈值的特征才进行存储
	public String thresholdFilter(String[] maxSimilar,String maxSimilarFeature){
		maxSimilarFeature=maxSimilar[0];
		String maxSimilarValue=maxSimilar[1];
		if (maxSimilarFeature.equals("客户") && Double.parseDouble(maxSimilarValue)>=CLIENT) {
			return maxSimilarFeature;
		}
		if (maxSimilarFeature.equals("供应商") && Double.parseDouble(maxSimilarValue)>=SUPPORT) {
			return maxSimilarFeature;
		}
		if (maxSimilarFeature.equals("研发") && Double.parseDouble(maxSimilarValue)>=DEVELOP) {
			return maxSimilarFeature;
		}
		if (maxSimilarFeature.equals("资金控股") && Double.parseDouble(maxSimilarValue)>=INVEST) {
			return maxSimilarFeature;
		}
		return null;
		
	}
	public String findSectionSentence(String text,String entity){
		//有缺陷需要改善
		int end=text.lastIndexOf("序", text.indexOf(entity));
		int start1=text.lastIndexOf("不适用", text.indexOf(entity));
		int start2=text.lastIndexOf("研发投入", text.indexOf(entity));
		String bufen="";
		if (end>0) {
			if (start1>0 && start2>0 && start2>start1) {
				bufen=text.substring(start2,end);
			}else {
				bufen=text.substring(start1+"不适用".length(), end);
			}
		}
		return bufen;
	}
	//业务联系和业务流向
	public String serviceParse(String maxSimilarFeature){
		String serviceRelation="";
		String serviceDirection="";
		String fundSupport="";
		if (maxSimilarFeature.equals("客户")) {
			serviceRelation="Yes";
			serviceDirection="向外";
			fundSupport="No";
		}
		if (maxSimilarFeature.equals("供应商")) {
			serviceRelation="Yes";
			serviceDirection="向内";
			fundSupport="No";
		}
		if (maxSimilarFeature.equals("研发")) {
			serviceRelation="No";
			serviceDirection="无";
			fundSupport="向外";
		}
		if (maxSimilarFeature.equals("资金控股")) {
			serviceRelation="No";
			serviceDirection="无";
			fundSupport="向内";
		}
		return serviceRelation+"、"+serviceDirection+"、"+fundSupport;
	}
	public String readText(String company){
		String dirPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing";
		String content=null;
		try {
			List<String> fileLists=ReadFiles.readDirs(dirPath);//返回文件的绝对路径
			for (String file : fileLists) {
				int preCom=file.indexOf("testdoing\\");
				int laterCom=file.indexOf(".txt");
				String Company=file.substring(preCom+"testdoing\\".length(), laterCom);
				if (company.equals(Company)) {
					//读取文件内容
					content=ReadFiles.readFiles(file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("处理完毕：");
		return content;

	}
	//测试
	public static void main(String[] args) {
		define();
		System.out.println(entityRelation.toString());
	}
}
