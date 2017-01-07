import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class listDemo {
	public static void main(String[] args) {
		List<String> list=new ArrayList<>();
		list.add("111");
		list.add("222");
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
	}
}
