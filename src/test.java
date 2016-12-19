

import java.io.IOException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.crypto.AEADBadTagException;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import com.txt.processing.WriteContent;

class compla implements Comparator<Double>{

	@Override
	public int compare(Double o1, Double o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
public class test {
	public static void main(String args[]){  
		String str="《丁酸氯维地平及乳剂》product_name";
		StringBuilder sb=new StringBuilder();
		sb.append(str);
		sb.insert(sb.indexOf("p"), "、");

		//ss[1]="product_name";
		System.out.println(sb.toString());
		String heh="";
		System.out.println(heh.length());//长度为0
		String hh=null;
		//System.out.println(hh.length());//出现异常//
		System.out.println("========++++++");
		TreeMap<Double, String> map=new TreeMap<>(new colp());
		HashMap<String, TreeMap<Double, String>> hash=new HashMap<>();
		map.put(2.4342, "jrje");
		map.put(3.526,"dwedrwe");
		map.put(1.526,"dwer");
		map.put(5.526,"y56ger");
		hash.put("1", map);
		hash.put("2", map);
		System.out.println(hash.toString());
		WriteContent wc=new WriteContent();
		String path="./hehe.txt";
		
		Set<Map.Entry<String, TreeMap<Double, String>>> set=hash.entrySet();
		Iterator<Map.Entry<String, TreeMap<Double, String>>> it=set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, TreeMap<Double, String>> en=it.next();
			/*en.getValue();
			en.getKey();*/
			wc.writeCon(en.getKey()+":\n"+en.getValue().toString(),path);
		}
		System.out.println("xiewan=====");
		/*Set<String> ket=hash.keySet();
		for (String string : ket) {
			
		}*/
		
		Set<Map.Entry<Double, String>> entryset=map.entrySet();
		for (Map.Entry<Double, String> entry : entryset) {
			System.out.println(entry.getValue());
			System.out.println(entry.getKey());
			break;
		}
		System.out.println(map.toString());
		HashMap<Double, String> hap=new HashMap<>();
		hap.put(2.4342, "jrje");
		hap.put(3.526,"dwedrwe");
		hap.put(1.526,"dwer");
		hap.put(5.526,"y56ger");
		Set<Map.Entry<Double, String>> entryset1=hap.entrySet();
		for (Map.Entry<Double, String> entry : entryset1) {
			System.out.println(entry.getValue());
			System.out.println(entry.getKey());
			break;
		}
		System.out.println(hap.toString());
		System.out.println("Stringbuilder++++++++++");
		StringBuilder de=new StringBuilder();
		de.append("heh");
		de.append("fbweufywe");
		de.setLength(0);
		de.append("ffffff");
		System.out.println(de.toString());
		TreeSet<Integer> re=new TreeSet<>();
		re.add(2);
		re.add(10);
		re.add(1);
		for (Integer integer : re) {
			System.out.println("set:"+integer);
		}
		System.out.println("。".length());
		System.out.println("！".length());
		System.out.println(" ".length());
		String stence="三、非主营业务分析单位：元  占利润总  是否具有  项目  金额  形成原因说明  额比例  可持续性投资收益。  6,934,311.90  16.48%  主要系公司本期收到徽商银行股份有限公司。  ";
		int inde=stence.indexOf("徽商银行股份有限公司");
		int start=stence.lastIndexOf(" ",inde);
		int start1=stence.lastIndexOf("。",inde);
		int end=stence.indexOf("。",inde);
		System.out.println("start:"+start+" start1:"+start1+",end:"+end);
		System.out.println(stence.substring(start+1,end+1));
		String fles="company_name 有 向外 无";
		String[] arrts=fles.split(" ");
		System.out.println(arrts.toString());
	}
}
class colp implements Comparator<Double>{
	
	public int compare(Double o1, Double o2) {
	// TODO Auto-generated method stub
	int num=o2.compareTo(o1);

	return num;
}
}

