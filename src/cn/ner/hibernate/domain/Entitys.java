package cn.ner.hibernate.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="entitys_inf")
public class Entitys {
	@Id @Column(name="entity_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer entityId;
	private String entityName;
	//定义关联实体
	@ManyToOne(targetEntity=Companys.class)
	@JoinColumn(name="company_id",referencedColumnName="company_id",nullable=false)
	private Companys companys;
	
	
	public Entitys() {
		
	}
	
	public Entitys(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}


	public Companys getCompanys() {
		return companys;
	}
	public void setCompanys(Companys companys) {
		this.companys = companys;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return entityName;
	}
}
