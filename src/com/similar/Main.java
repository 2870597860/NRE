package com.similar;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Simhash测试
 * 
 * @author litaoxiao
 * 
 */
public class Main {
	public static void main(String[] args) throws IOException {
		List<String> ls = FileUtils.readLines(new File("test"));
		Simhash simhash = new Simhash(4, 20);
		String demo="company_name SBV 实现 VOB 营业收入*, COO 实现 VOB 净利润*, COO 增长 VOB*。";
		//String demo1="company_name SBV 实现 VOB 营业收入4 * , COO 实现 VOB 净利润237.88万元 * , COO 增长 VOB 33.58% *。";
		Long simhashVal = simhash.calSimhash(demo);
		//Long simhashVal1 = simhash.calSimhash(demo1);
		simhash.store(simhashVal, demo);
		//simhash.store(simhashVal1, demo);
		System.out.println(Long.toBinaryString(simhashVal));
		for (String content : ls) {
			//System.out.println(simhashVal);
			Long simhashVal2 = simhash.calSimhash(content);
			System.out.println(Long.toBinaryString(simhashVal2));
			System.out.println(simhash.isDuplicate(content, null));
			
		}
	}
}
