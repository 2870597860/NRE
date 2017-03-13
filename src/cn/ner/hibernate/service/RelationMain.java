package cn.ner.hibernate.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cn.ner.hibernate.Util.HibernateUtil;
import cn.ner.hibernate.domain.Companys;
import cn.ner.hibernate.domain.Entitys;

public class RelationMain {
	private Session session=null;
	private Transaction tr=null;

	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Transaction getTr() {
		return tr;
	}
	public void setTr(Transaction tr) {
		this.tr = tr;
	}

	public void dataOperatorBefore(){
		SessionFactory sessionFac=HibernateUtil.getSessionFactory();
		session=sessionFac.openSession();
		tr=session.beginTransaction();
	}
	public void dataOperator(HashMap<String, HashMap<String, String>> classifacResults){

		Set<String> companys=classifacResults.keySet();

		for (String company : companys) {
			//每个公司就是一个对象	
			Companys comp=null;			
			List<String> re=new ArrayList<>();
			//存放关系以对应的对象
			HashMap<String, Companys> cacheObject=new HashMap<>();
			//获取公司相应的实体分类结果
			HashMap<String, String> relationResults=classifacResults.get(company);
			Set<String> entityRealtion=relationResults.keySet();
			for (String entityStr : entityRealtion) {
				String relation=relationResults.get(entityStr);
				if (re.size()<=0) {
					comp=new Companys();
					comp.setCompanyName(company);
					session.save(comp);
					re.add(relation);
					cacheObject.put(relation, comp);
				}
				if (re.size()>0) {
					if (!re.toString().contains(relation)) {
						comp=new Companys();
						comp.setCompanyName(company);
						session.save(comp);
						re.add(relation);
						cacheObject.put(relation, comp);
					}
				}
				comp=cacheObject.get(relation);
				
				//创建瞬态Entitys对象
				Entitys en=new Entitys(entityStr);
				//获取关系
				comp.setRelation(relation);
				en.setCompanys(comp);
				//再持久化Entitys对象
				session.persist(en);
			}			
		}
		tr.commit();
	}
	public void dataOperatorAfter(){

		if (session!=null) {
			session.close();
		}
	}
	//测试
	public static void main(String[] args) {
		RelationMain rm=new RelationMain();
		HashMap<String, HashMap<String, String>> classifacResults=new HashMap<>();
		HashMap<String, String> relationResults=new HashMap<>();
		relationResults.put("安徽华源医药股份有限公司", "客户");
		relationResults.put("宁波天衡", "客户");
		relationResults.put("安徽华源医药股份有限公司", "客户");
		relationResults.put("宁波天衡", "客户");
		relationResults.put("美洛西林钠舒巴坦钠", "研发");
		relationResults.put("《注射用头孢孟多酯钠》", "研发");
		relationResults.put("美洛西林钠舒巴坦钠", "研发");
		relationResults.put("《注射用头孢孟多酯钠》", "研发");
		relationResults.put("瑞阳制药有限公司", "供应商");
		relationResults.put("武汉人福药", "供应商");
		relationResults.put("瑞阳制药有限公司", "供应商");
		relationResults.put("武汉人福药", "供应商");
		classifacResults.put("aa", relationResults);
		relationResults.put("蚌埠医学院第二附属医院", "客户");
		relationResults.put("安徽华源医药股份有限公司", "客户");
		relationResults.put("《注射用帕瑞昔布钠原料及制剂》", "研发");
		relationResults.put("丁酸氯维地平及乳剂", "研发");
		relationResults.put("安徽天星医药集团有限公司", "供应商");
		relationResults.put("安徽华宁医药物流有限公司", "供应商");
		classifacResults.put("bb", relationResults);

		rm.dataOperatorBefore();
		rm.dataOperator(classifacResults);
		rm.dataOperatorAfter();

	}
}
