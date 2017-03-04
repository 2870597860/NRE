
  
import java.util.HashMap;  
import java.util.Map;  
import java.util.Set;  
  
/** 
 * 特征向量的相似性匹配算法 
 * Created by panther on 15-7-20. 
 */  
public class Similarity {  
    Map<String, int[]> vectorMap = new HashMap<String, int[]>();  
  
    int[] tempArray = null;  
  
    public Similarity(String[] string1, String[] string2) {  
  
        for (String str1 : string1) {  
            if (vectorMap.containsKey(str1)) {  
                vectorMap.get(str1)[0]++;  
            } else {  
                tempArray = new int[2];  
                tempArray[0] = 1;  
                tempArray[1] = 0;  
                vectorMap.put(str1, tempArray);  
            }  
        }  
        for (String str2 : string2) {  
            if (vectorMap.containsKey(str2)) {  
                vectorMap.get(str2)[1]++;  
            } else {  
                tempArray = new int[2];  
                tempArray[0] = 0;  
                tempArray[1] = 1;  
                vectorMap.put(str2, tempArray);  
            }  
        }  
    }  
  
    // 求余弦相似度  
    public double sim() {  
        double result = 0;  
        result = pointMulti(vectorMap) / sqrtMulti(vectorMap);  
        return result;  
    }  
  
    private double sqrtMulti(Map<String, int[]> paramMap) {  
        double result = 0;  
        result = squares(paramMap);  
        result = Math.sqrt(result);  
        return result;  
    }  
  
    // 求平方和  
    private double squares(Map<String, int[]> paramMap) {  
        double result1 = 0;  
        double result2 = 0;  
        Set<String> keySet = paramMap.keySet();  
        for (String character : keySet) {  
            int temp[] = paramMap.get(character);  
            result1 += (temp[0] * temp[0]);  
            result2 += (temp[1] * temp[1]);  
        }  
        return result1 * result2;  
    }  
  
    // 点乘法  
    private double pointMulti(Map<String, int[]> paramMap) {  
        double result = 0;  
        Set<String> keySet = paramMap.keySet();  
        for (String str : keySet) {  
            int temp[] = paramMap.get(str);  
            result += (temp[0] * temp[1]);  
        }  
        return result;  
    }  
  
    public static void main(String[] args) {  
        String[] sgong = {"资料","采购","金额","供应 ","总额 ","比例","供应商","情况"}; 
        String[] ske = {"资料","销售 ","金额","合计","供应商","总额","比例","客户","情况"}; 
        String[] s1 = {"供应","供应商","采购"};  
        String[] s2 = {"销售","客户","金额"};
        String[] s3 = {"资料","销售","客户","金额","合计","情况","总额"};
        String[] s4 = {"前五名","客户","情况","收入","营业"};
        String[] s5 = {"子公司"};
        String[] s6 = {"公司"};
        
        Similarity similarity = new Similarity(s3, sgong); 
        Similarity similarity2 = new Similarity(s5, s6); 
        double sim=similarity.sim();
        double sim2=similarity2.sim();
        System.out.println(";;;;"+sim+" "+sim2);  
    }  
  
}  
