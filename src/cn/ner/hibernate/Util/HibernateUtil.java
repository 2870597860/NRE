package cn.ner.hibernate.Util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static SessionFactory sessionFac=getbuildSessionFaction();
	public static SessionFactory getSessionFactory(){
		return sessionFac;
	}
	private static SessionFactory getbuildSessionFaction(){
		Configuration config=new Configuration().configure();
		ServiceRegistry sr=new StandardServiceRegistryBuilder()
				.applySettings(config.getProperties()).build();
		return config.buildSessionFactory(sr);		
	}
}
