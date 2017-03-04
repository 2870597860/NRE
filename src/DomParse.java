import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.swing.text.AbstractDocument.LeafElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;

import com.RE.dp.FindTextSetence;

public class DomParse {
	static DomParse dp=new DomParse();
	public static void main (String[] args) throws Exception{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File("test.xml"));
		// 获取根元素节点
		Element root = document.getRootElement();
		System.out.println("Root: " + root.getName());
		//获取doc节点
		Element docNode=dp.findDocNode(root);
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
					System.out.println("id:"+element2.attributeValue("id"));
				}
				dp.getSBV_VOB_DP(wordNodeList);
			}
		}

	}
	/**
	 * 获取依存分析关系模式（SBV-VOB形式）
	 * @param wordNodeList 句子（节点sent）下的所有word节点（包含属性）
	 */
	public void getSBV_VOB_DP(List<Element> wordNodeList){
		for (Element wordNode : wordNodeList) {
			String strRelate=wordNode.attributeValue("relate");
			if (strRelate.equals("HED")) {
				String cont=wordNode.attributeValue("cont");
				String idHED=wordNode.attributeValue("id");
			}
			if (strRelate.equals("SBV")) {
				//获取SBV部分
				getSBV_VOB(wordNodeList, wordNode);
			}
			if (strRelate.equals("VOB")||strRelate.equals("POB")) {
				//获取VOB或POB部分
				getSBV_VOB(wordNodeList, wordNode);
			}
		}
	}
	//获取sbv部分和VOB或者POB部分
	public void getSBV_VOB(List<Element> wordNodeList,Element wordNode){
		TreeMap<Integer, String> treeMap=new TreeMap<>();
		String cont=wordNode.attributeValue("cont");
		String id=wordNode.attributeValue("id");
		String parent=wordNode.attributeValue("parent");
		//获取sbv整个相关联词语（就是实体名）
		treeMap.put(Integer.parseInt(id), cont);
		treeMap=dp.get_cont(wordNodeList, id,treeMap);
		System.out.println(treeMap);
	}
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
				if (Integer.parseInt(sbvId)==Integer.parseInt(subParent)) {
					if ((Integer.parseInt(sbvId)-Integer.parseInt(subId))==1&&
							subrelate.equals("ATT")) {
						treeMap.put(Integer.parseInt(subId), wordNode.attributeValue("cont"));
						diguiGet_cont(wordNodeList, subId, treeMap);
					}
				}
			}
		}
		return treeMap;
	}
	public void diguiGet_cont(List<Element> wordNodeList,String id1,TreeMap<Integer,String> treeMap){
		for (Element element : wordNodeList) {
			String subId=element.attributeValue("id");
			String subrelate=element.attributeValue("relate");
			if ((Integer.parseInt(id1)-Integer.parseInt(subId))==1&&
					subrelate.equals("ATT")) {
				treeMap.put(Integer.parseInt(subId), element.attributeValue("cont"));
				diguiGet_cont(wordNodeList,subId,treeMap);
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

}
