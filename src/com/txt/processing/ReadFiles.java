package com.txt.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadFiles {
	
	//读取目录中的所有文件
	public static List<String> readDirs(String filepath) throws FileNotFoundException, IOException {
		//定义读取文件集合
		List<String> fileList = new ArrayList<String>();
		try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("输入的参数应该为[文件夹名]");
                System.out.println("filepath: " + file.getAbsolutePath());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        //System.out.println("filepath: " + readfile.getAbsolutePath());
                        fileList.add(readfile.getAbsolutePath());
                    } else if (readfile.isDirectory()) {
                        readDirs(filepath + "\\" + filelist[i]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return fileList;
    } 
	//读取每个文件中的文本内容
	 public static String readFiles(String file) throws FileNotFoundException, IOException {
	        StringBuffer sb = new StringBuffer();
	        InputStreamReader is = new InputStreamReader(new FileInputStream(file), "utf-8");
	        BufferedReader br = new BufferedReader(is);
	        String line =null;
	        String regEx = "['   ']+";//匹配文章中连续的多个空格
			Pattern p = Pattern.compile(regEx);
	        while ((line=br.readLine()) != null) {
	        	Matcher m = p.matcher(line);
	        	line=m.replaceAll("  ");//将连续的多个空格删除只保留一个
				sb.append(line);
	        }
	        int index=0;
	        int i=0;
	        while(index<sb.length()){
	        	index=4700;
	        	i++;
	        	index*=i;
	        	if (index<sb.length()) {
	        		int indexSpace=sb.lastIndexOf(" ", index);
	        		sb.insert(indexSpace, "**");
				}
	        	
	        }
	        br.close(); 
	        return sb.toString();
	    }
}
