package com.entity.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.python.antlr.PythonParser.listmaker_return;
import org.python.antlr.PythonParser.return_stmt_return;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.nlp.parser.dep.DependencyTree;
import edu.fudan.nlp.parser.dep.JointParser;


public class SentenceParse {
	
	//
	public  ArrayList<String> depparser(String text,String entityName){
		String sentence="";
		ArrayList<String> list=null;
		if(!entityName.matches("[a-zA-Z]+")) {
			//text=GetEntity.readEntity(file);
			int index=text.indexOf(entityName);
			if (index>0) {
				sentence=findSetence(text, index);
				 if(sentence.contains("徽商银行股份有限公司")) {
				sentence=sentence.replaceAll(" ", "");
				list=test_dep(sentence);
				}
			}
			/*String path="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\CacheContent\\Sentence.txt";
			if (index>0) {
				WriteSentence wr=new WriteSentence();
				wr.writeCon(sentence, path);
			}*/
		}
		return list;


	}
	/**
	 * 测试语义依存
	 * 只输入句子，不带词性
	 * @throws Exception 
	 */
	public  ArrayList<String> test_dep(String word){  
		JointParser parser;
		ArrayList<String> list=new ArrayList<>();
		try {
			parser = new JointParser("models/dep.m");
			CWSTagger cwst = new CWSTagger("./models/seg.m",new Dictionary("./models/dict.txt"));
			POSTagger tag = new POSTagger(cwst,"models/pos.m");
			String[][] s = tag.tag2Array(word);
			/*DependencyTree tree = parser.parse2T(s[0],s[1]);
			System.out.println(tree.toString());*/
			String stree = parser.parse2String(s[0],s[1],true);
			String[] streeArr=stree.split("\n");
			String[][] sstree=new String[4][];
			for (int i = 0; i < streeArr.length; i++) {
				sstree[i]=streeArr[i].split(" ");
			}

			String[] sstree3= sstree[3];
			for (int i = 0; i < sstree3.length; i++) {
				if (sstree3[i].equals("核心词")||sstree3[i].equals("主语")||sstree3[i].equals("宾语")||sstree3[i].equals("补语")) {
					list.add(sstree[0][i]);
				}
			}
			System.out.println(list);


		} catch (Exception e) {         
			e.printStackTrace();
		}
		return list;
	}

	//获取实体所在的句子或段落
	public String findSetence(String text,int index){
		//获取实体所在的句子或段落
		String[] str={"。","！"," "};
		int startIndex=index;
		TreeSet<Integer> tre=new TreeSet<>();
		for (int i = 0; i < str.length; i++) {
			int temp=text.lastIndexOf(str[i],index);
			if (temp>0) {
				tre.add(temp);
			}	
		}
		for (Integer integer : tre) {
			startIndex=integer;
		}
		int endIndex=text.indexOf("。",index);
		String sentence=text.substring(startIndex+1, endIndex+1);
		return sentence;
	}
	public static void main(String[] args) throws Exception {
		//parser = new JointParser("models/dep.m");
		SentenceParse sp=new SentenceParse();
		String word = "主要系公司本期收到徽商银行股份有限公司2014年度利润分红款。";
		sp.test_dep(word);

	}
}