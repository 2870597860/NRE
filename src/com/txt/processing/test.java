package com.txt.processing;

import java.io.IOException;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class test {
	public static void main(String args[])  
    {  
          
		try {
			System.out.println("开始执行python：");  
			Process proc = Runtime.getRuntime().exec("python  D:\\A_summer_install\\java\\workspace\\pytest\\src\\test01.py");
			proc.waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println("python执行结束");
  
    }
	
}
