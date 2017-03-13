package cn.ner.readwrite;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 对原文进行过滤，去除不必要的部分,
 * 将需要的部分写入另一文本中，为python的entity识别做准备
 * testdoing中存放待处理的年报文本，
 * writeContent.java中CacheContent为处理后写入存储的路径
 * @author Administrator
 *
 */
public class ReadReportText {
	private String dirPath;
	public ReadReportText() {}
	public ReadReportText(String dirPath) {
		super();
		this.dirPath = dirPath;
	}
	public HashMap<String, String> readReport(){
		HashMap<String, String> reportTextMap=new HashMap<>();
		try {
			List<String> fileLists=ReadFiles.readDirs(dirPath);//返回文件的绝对路径
			String content=null;
			int textCount=0;
			for (String file : fileLists) {
				++textCount;
				int preCom=file.indexOf("\\testdoing\\");
				int laterCom=file.indexOf(".txt");
				String companyName=file.substring(preCom+"\\testdoing\\".length(), laterCom);
				//读取文件内容
				content=ReadFiles.readFiles(file);
				int textStart=content.indexOf("主营业务分析");
				int textEnd=content.indexOf("十、接待调研");
				if (textStart<0) {
					textStart=0;
				}
				if (textEnd<0) {
					textEnd=content.indexOf("可能面对的风险");
					if (textEnd<0) {
						textEnd=content.length();
					}
				}
				/*
				 * 由于实体识别每次文本字数有限所以将文本用（**）隔开，分成三段
				 */
				content=content.substring(textStart, textEnd);
				reportTextMap.put(companyName, content);
			}
			System.out.println("读取完毕!读取文章总数为："+textCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reportTextMap;
	}
	public static void main(String[] args) {
		String dirPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing";//读取文件路径
		ReadReportText rrt=new ReadReportText(dirPath);
		rrt.readReport();
	}
}
