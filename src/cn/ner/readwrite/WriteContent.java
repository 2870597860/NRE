package cn.ner.readwrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class WriteContent {
	public  void writeCon(String content,String path){
		OutputStreamWriter out;
		File file=new File(path);
		try {
			if(!file.exists()){  
				file.createNewFile();
			}
			out=new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			out.write(content);
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	//可以在原有文件末尾进行追加
	public  void writeConAppend(String content,String path){
		OutputStreamWriter out;
		File file=new File(path);
		try {
			if(!file.exists()){  
				file.createNewFile();
			}
			out=new OutputStreamWriter(new FileOutputStream(file,true), "utf-8");
			out.write(content);
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
}
