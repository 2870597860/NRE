package com.pythonJava;


/** 
 * 供python脚本调用的java类 
 * @author webim 
 * 
 */  
public class SayHello  
{  
	private String userName;  

	public String getUserName()  
	{  
		return userName;  
	}  

	public void setUserName(String userName)  
	{  
		this.userName = userName;  
	}  

	public void say(int i)  
	{  
		System.out.println(i + ":Hello " + userName);  
	}  
}  
