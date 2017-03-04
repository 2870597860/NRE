package com.RE.nameEntityRecoMain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.RE.ReadText.ReadReportText;
import com.RE.writetext.WriteContent;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class StartMain {
	public static void main(String[] args) {
		String dirPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing";//读取文件路径
		ReadReportText rrt=new ReadReportText(dirPath);
		HashMap<String, String> reportTextMap=rrt.readReport();//存储公司以及对应的文章内容
		//companyAndEntity_Type存储公司及对应的实体和类型
		HashMap<String, HashMap<String, String>> companyAndEntity_Type=new HashMap<>();
		Set<String> companyReport=reportTextMap.keySet();
		for (String company : companyReport) {
			System.out.println(company);
			String reportText=reportTextMap.get(company);
			System.out.println(reportText);
			String[] reportTextArr=reportText.split("\\*\\*");
			try {
				HashMap<String, String> entityAndType=NameEntityRe.NameEntityReMain(reportTextArr);
				companyAndEntity_Type.put(company, entityAndType);
				Set<Map.Entry<String, String>> ET=entityAndType.entrySet();
				StringBuilder sb=new StringBuilder();
				for (Map.Entry<String, String> entry : ET) {
					sb.append(entry+"\n");
				}
				//将companyAndEntity_Type写到文本中
				WriteContent wc=new WriteContent();
				wc.writeCon(sb.toString(),"./EntityAll/entity_"+company+".txt");
				
			} catch (JSONException | UnirestException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			System.out.println("文章分析完毕！");
			Unirest.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
