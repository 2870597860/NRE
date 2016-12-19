package com.entity.processing;

  
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
        String[] s2 = {"情况","进入","注册","分类","名称","程序","药品","适应症","研发","技术"}; 
        String[] s1 = {"研发","技术","药品"};  
        Similarity similarity = new Similarity(s1, s2);  
        System.out.println(similarity.sim());  
    }  
  
}  
