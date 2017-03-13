package cn.ner.RealtionPatternMain3;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.RE.dp.DepParsing;
import com.txt.processing.ReadFiles;

public class DomParse {
	String company="";
	/**
	 * 获取依存分析关系模式（SBV-VOB形式）
	 * @param wordNodeList 句子（节点sent）下的所有word节点（包含属性）
	 */
	public void getSBV_VOB_DP(List<Element> wordNodeList,ArrayList<String> arrList){
		StringBuffer sbRP=new StringBuffer();
		int flagSBV=0;
		TreeSet<String> idSBVSet=new TreeSet<>();
		for (Element wordNode : wordNodeList) {
			String strRelate=wordNode.attributeValue("relate");
			if (strRelate.equals("SBV")) {
				//获取SBV部分
				boolean flag=true;
				String idSBV=wordNode.attributeValue("id");
				idSBVSet.add(idSBV);
				++flagSBV;
				getSBV_VOB(wordNodeList, wordNode,flag,sbRP);
				sbRP.append(" SBV ");
				//对一个句子中的多个SBV进行处理
				if (flagSBV>1) {//若句子中出现多个sbv
					String ids=null;
					int SBVChazhi=0;
					for (String string : idSBVSet) {
						if (ids==null) {
							ids=string;
						}else {
							SBVChazhi=Integer.parseInt(string)-Integer.parseInt(ids);							
						}
					}
					//String cont=wordNode.attributeValue("cont");
					/*if (cont.equals("制药")) {
						System.out.println("+++");
					}*/
					int index=sbRP.indexOf(":");
					if (SBVChazhi==1) {
						sbRP.delete(index,sbRP.indexOf("SBV",index)+"SBV ".length());
					}else {
						//System.out.println();
						int secondMaoHao=sbRP.indexOf(":",index+":".length());
						if (secondMaoHao>0) {
							sbRP.delete(secondMaoHao,sbRP.indexOf("SBV", secondMaoHao)+"SBV ".length());
						}
						//sbRP.append(",");
					}
					idSBVSet.remove(ids);
				}
			}
			if (strRelate.equals("HED")) {
				String cont=wordNode.attributeValue("cont");
				String idHED=wordNode.attributeValue("id");
				sbRP.append(cont);
			}
			if (strRelate.equals("COO")) {
				boolean flag=false;
				sbRP.append(","+" COO ");
				getSBV_VOB(wordNodeList, wordNode,flag,sbRP);

			}
			/*
			 * 获取关系为POB和VOB部分的cont，并去除关系为COO紧挨的POB的多余部分
			 */
			//庆余堂:null SBV 实现 VOB 营业收入21592.15万元,下降,实现 VOB 净利润339.31万元,下降 VOB 65.92%。
			if (strRelate.equals("VOB")||strRelate.equals("POB")) {
				//获取VOB或POB部分
				boolean flag=false;
				String idPOB=wordNode.attributeValue("id");
				if (strRelate.equals("VOB")) {
					sbRP.append(" VOB ");
					getSBV_VOB(wordNodeList, wordNode,flag,sbRP);
				}
				if (strRelate.equals("POB")) {
					String biaozhi="no";
					for (Element wordNodep : wordNodeList){
						String strRelateCOO=wordNodep.attributeValue("relate");
						//修饰COO的POB（介宾关系）进行去除，属于噪音
						if (strRelateCOO.equals("COO")) {
							String idCOO=wordNodep.attributeValue("id");
							if ((Integer.parseInt(idCOO)-Integer.parseInt(idPOB))==1) {
								biaozhi="yes";
								break;
							}
						}
						//修饰HED的POB（介宾关系）进行去除，属于噪音
						if (strRelateCOO.equals("HED")) {
							String idCOO=wordNodep.attributeValue("id");
							if ((Integer.parseInt(idCOO)-Integer.parseInt(idPOB))==1) {
								biaozhi="yes";
								break;
							}
						}
					}
					if (biaozhi.equals("yes")) {
						continue;
					}
					if (biaozhi.equals("no")) {
						sbRP.append(" POB ");
						getSBV_VOB(wordNodeList, wordNode,flag,sbRP);
					}
				}
				sbRP.append(" * ");
			}
		}

		sbRP.append("。");
		if (sbRP.indexOf("COO")>0) {
			if (!(sbRP.substring(sbRP.indexOf("COO"), sbRP.length()).contains("VOB"))) {
				sbRP.replace(sbRP.indexOf("COO"), sbRP.length()-1, " * ");
			}
		}
		int xinghao=sbRP.indexOf(" * ");
		if (xinghao>0) {
			if (sbRP.indexOf(" * ",xinghao+" * ".length())>0) {
				if (!(sbRP.substring(xinghao,sbRP.indexOf(" * ",xinghao)).matches("^[\u4e00-\u9fa5]"))) {
					sbRP.replace(xinghao,sbRP.indexOf(" * ",xinghao+" * ".length()), "");
				}
			}
		}
		if (sbRP.toString().contains("null")) {
			int index=sbRP.indexOf("null");
			sbRP.replace(index,index+"null".length(), "company_name");
		}
		System.out.println(sbRP);
		arrList.add(sbRP.toString());
		//===================================郑州炜盛电子科技有限公司:null SBV 为 VOB 全资子公司。
	}
	//获取sbv部分和VOB或者POB部分
	public void getSBV_VOB(List<Element> wordNodeList,Element wordNode,boolean flag,
			StringBuffer sbRP){
		GetEnType getET=new GetEnType();
		TreeMap<Integer, String> treeMap=new TreeMap<>();
		String cont=wordNode.attributeValue("cont");
		String id=wordNode.attributeValue("id");
		String parent=wordNode.attributeValue("parent");
		//获取sbv整个相关联词语（就是实体名）
		treeMap.put(Integer.parseInt(id), cont);
		treeMap=get_cont(wordNodeList, id,treeMap);
		StringBuffer sbCache=new StringBuffer();//存放实体
		Set<Integer> set=treeMap.keySet();
		for (Integer integer : set) {
			String str=treeMap.get(integer);
			sbCache.append(str);
		}
		if (flag) {
			String testStr=sbCache.toString();
			//System.out.println(testStr);	
			/*if (sbCache.toString().equals("人民制药")) {
				System.out.println(sbCache);
			}*/
			String entityType=getET.startRead(company).get(sbCache.toString());//获取实体对应的实体类型
			sbCache.append(":"+entityType);
		}
		sbRP.append(sbCache.toString());
		/*System.out.println(sbCache);
		System.out.println(treeMap);//输出
*/	}
	/**
	 * 获取sbv整个相关联词语（就是实体名）
	 * @param wordNodeList 句子（节点sent）下的所有word节点（包含属性
	 * @param sbvId 关系为"sbv"(relate="sbv")所在的id
	 * @param treeMap 存储SBV所在部分的词语
	 * @return
	 */
	public TreeMap<Integer,String> get_cont(List<Element> wordNodeList,String sbvId,TreeMap<Integer,String> treeMap){
		for (Element wordNode : wordNodeList){
			String subId=wordNode.attributeValue("id");
			if (Integer.parseInt(sbvId)>Integer.parseInt(subId)) {
				//String subId=wordNode.attributeValue("id");
				String subParent=wordNode.attributeValue("parent");
				String subrelate=wordNode.attributeValue("relate");
				//if (Integer.parseInt(sbvId)==Integer.parseInt(subParent)) {
					if ((Integer.parseInt(sbvId)-Integer.parseInt(subId))==1&&
							subrelate.equals("ATT")) {
						treeMap.put(Integer.parseInt(subId), wordNode.attributeValue("cont"));
						diguiGet_cont(wordNodeList, subId, treeMap);
					}
				//}
			}
		}
		return treeMap;
	}
	public void diguiGet_cont(List<Element> wordNodeList,String id1,TreeMap<Integer,String> treeMap){
		for (Element element : wordNodeList) {
			String subId=element.attributeValue("id");
			if (Integer.parseInt(id1)>Integer.parseInt(subId)) {
				String subrelate=element.attributeValue("relate");
				if ((Integer.parseInt(id1)-Integer.parseInt(subId))==1&&
						subrelate.equals("ATT")) {
					treeMap.put(Integer.parseInt(subId), element.attributeValue("cont"));
					diguiGet_cont(wordNodeList,subId,treeMap);
				}
			}
		}
	}
	public  Element findDocNode(Element root){
		Iterator<Element> subNode=root.elementIterator();
		Element DocNode=null;
		while (subNode.hasNext()) {
			DocNode = (Element) subNode.next();
			if (DocNode.getName().equals("doc")) {
				return DocNode;
			}
		}
		return null;

	}

	public List<String> ParseStart(Document document,int[] count){
		//解析XML形式的文本,得到document对象.                
		/*try {
			document = DocumentHelper.parseText(sbSentenceSet);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ArrayList<String> arrList=new ArrayList<>();
		if (document!=null) {
			// 获取根元素节点
			Element root = document.getRootElement();
			System.out.println("Root: " + root.getName());
			//获取doc节点
			Element docNode=findDocNode(root);
			if (docNode!=null) {
				//doc的第一个子节点就是para节点(获取名字为指定名称的第一个子元素)
				Element paraNode=docNode.element("para");
				List<Element> sentNodeList=paraNode.elements();
				int i=0;
				for (Element element : sentNodeList) {
					++i;
					System.out.println("第"+i+"节点："+element.getName());
					//System.out.println("当前节点的内容："+element.getTextTrim());//当前节点名称  
					List<Element> wordNodeList=element.elements();
					for (Element element2 : wordNodeList) {
						//System.out.println("id:"+element2.attributeValue("id"));
					}
					getSBV_VOB_DP(wordNodeList,arrList);
				}
				count[0]+=i;
			}
		}
		return arrList;

	}
	/**
	 * 解析xml文本
	 * @param sbSentenceSet xml文本
	 */
	public HashMap<String, List<String>> XMLParseStart(String sbSentenceSet){
		//读取XML文件,获得document对象 
		SAXReader saxReader = new SAXReader();
		Document document=null;
		List<String> fileLists=null;
		HashMap<String, List<String>> xmlParseResults=new HashMap<>();
		try {
			//返回文件的绝对路径
			fileLists = ReadFiles.readDirs("SentenceXML");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (fileLists.size()>0) {
			int[] count=new int[1];
			count[0]=0;
			for (String file : fileLists){
				int len=file.indexOf("\\SentenceXML\\");
				company=file.substring(len+"\\SentenceXML\\".length(),
						file.indexOf(".xml"));
				System.out.println("\n========="+company+":");
				try {
					document = saxReader.read(new File(file));
					List<String> xmlParseResult=ParseStart(document,count);
					//System.out.println(xmlParseResult);
					xmlParseResults.put(company, xmlParseResult);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("读取文本句子的关系模式总数为："+count[0]);
		}
		return xmlParseResults;
	}
	//测试
	public static void main (String[] args) throws Exception{
		String sentence="<xml4nlp>"
				+"<doc>"
				+"<para id=\"0\">"
				+"<sent id=\"0\" cont=\"徽商银行股份有限公司为本公司的参股公司。\">"
				+ "<word id=\"0\" cont=\"徽商\" pos=\"nz\" parent=\"3\" relate=\"ATT\"/>"
				+"<word id=\"1\" cont=\"银行\" pos=\"n\" parent=\"2\" relate=\"ATT\" />"
				+"<word id=\"2\" cont=\"股份\" pos=\"n\" parent=\"3\" relate=\"ATT\" />"
				+"<word id=\"3\" cont=\"有限公司\" pos=\"n\" parent=\"4\" relate=\"SBV\" />"
				+"<word id=\"4\" cont=\"为\" pos=\"p\" parent=\"-1\" relate=\"HED\" />"
				+"<word id=\"5\" cont=\"本\" pos=\"r\" parent=\"6\" relate=\"ATT\" />"
				+"<word id=\"6\" cont=\"公司\" pos=\"n\" parent=\"9\" relate=\"ATT\" />"
				+"<word id=\"7\" cont=\"的\" pos=\"u\" parent=\"6\" relate=\"RAD\" />"
				+"<word id=\"8\" cont=\"参股\" pos=\"v\" parent=\"9\" relate=\"ATT\" />"
				+"<word id=\"9\" cont=\"公司\" pos=\"n\" parent=\"4\" relate=\"VOB\" />"
				+"<word id=\"10\" cont=\"。\" pos=\"wp\" parent=\"4\" relate=\"WP\" />"
				+"</sent>"//是xml文本"
				+ "</para>"
				+ "</doc>+"
				+ "</xml4nlp>";
		DomParse dp=new DomParse();
		File filetest=new File("test.xml");
		int[] count=new int[1];
		count[0]=0;
		SAXReader saxReader = new SAXReader();
		Document documenttest = saxReader.read(filetest);
		dp.ParseStart(documenttest,count);
		//dp.XMLParseStart(sentence);
	}
}
