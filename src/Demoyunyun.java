import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
//定义比较器降序排列
class MycomplaYun implements Comparator<Integer>{

	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		int num=o2.compareTo(o1);
		return num;
	}

}
public class Demoyunyun {
	public static void main(String[] args) {
		int[] arr={19,23,12,11,34,25,10,4,7,9};
		TreeSet<Integer> liSet=new TreeSet<>(new MycomplaYun());
		for (int i = 0; i < arr.length; i++) {
			liSet.add(arr[i]);
		}
		System.out.println(liSet);
		for (Integer integer : liSet) {
			int j=0;
			for (int i = 0; i < arr.length; i++) {
				if (integer.equals(arr[i])) {
					System.out.println(arr[i]+"在原数组中的坐标为："+i);
				}
			}
			j++;
			if (j>=5) {
				break;
			}
		}
		
 	}
}
