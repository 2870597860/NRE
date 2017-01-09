package com.pythonJava;

import java.util.HashMap;
import java.util.Map;

public enum TestExecPython {
	INSTANCE;  
    
    public void test()  
    {  
        String scriptFile = "E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\CacheContent";  
       // Map<String,String> properties = new HashMap<String,String>();  
        //properties.put("userName", "Demo");  
          
        //ExecPython.INSTANCE.execute(scriptFile, properties);  
        ExecPython.INSTANCE.executeEntity(scriptFile);  
    }  
}
