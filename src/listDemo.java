import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

public class listDemo {
	public static void main(String[] args) {
		List<String> list=new ArrayList<>();
		list.add("111");
		list.add("222");
		String[] heheArr=list.toArray(new String[list.size()]);
		for (int i = 0; i < heheArr.length; i++) {
			System.out.println("heheArr中的值为："+heheArr[i]);
		}
		for (String string : list) {
			System.out.println(string);
		}
		System.out.println(list);
		String arr=list.toString();
		String[] arrList=arr.split(",");
		for (int i = 0; i < arrList.length; i++) {
			System.out.println(arrList[i]);
		}
		System.out.println(arr);
		System.out.println(Arrays.asList(arr.replace("[", "").replace("]", "")));
		String str="广东工龙泉广东程";
		System.out.println(str.length());
		//cdscsc
		String[] array1={"马超","马云","关羽","刘备","张飞"};
		System.out.println(Arrays.asList(array1));
		StringBuffer sb=new StringBuffer();
		sb.append(Arrays.asList(array1));
		sb.append(Arrays.asList(array1));
		System.out.println(sb);
		String[] arr2 = new String[] {"str1", "str2"};  
		List<String> list2 = Arrays.asList(arr2);
		System.out.println(list2);
		
		HashMap<String, String> map=new HashMap<>();
		map.put("a", "a1");
		map.put("b", "b1");
		map.put("c", "c1");
		Set<Map.Entry<String, String>> setMap=map.entrySet();
		StringBuilder ssbb=new StringBuilder();
		for (Map.Entry<String, String> entry : setMap) {
			//System.out.println(entry);
			ssbb.append(entry+"\n");
		}
		System.out.println("========");
		System.out.println(ssbb.toString());
		HashMap<String , String> mapw=new HashMap<>();
		mapw.put("dasd", null);
		System.out.println(mapw.get("dasd"));
		System.out.println("A股product_name".length());
		String fefe="江铃控股有限公司                                   75,802                73,013             3.82%";
		System.out.println(fefe.replaceAll(" +", " "));
	}
}
