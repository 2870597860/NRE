package cn.ner.hibernate.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="companys_inf")
public class Companys {
	@Id @Column(name="company_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer companyId;
	private String companyName;
	private String relation;

	//定义该类实体的关联的Address实体
	@OneToMany(targetEntity=Entitys.class,mappedBy="companys")
	//映射外键列，次数映射的外键列将会添加到关联实体对应的数据表中
	private List<Entitys> entitys=new ArrayList<Entitys>();

	public Companys() {
	}
	public Companys(String companyName, String relation) {
		this.companyName = companyName;
		this.relation = relation;
	}
	

	public List<Entitys> getEntitys() {
		return entitys;
	}
	public void setEntitys(List<Entitys> entitys) {
		this.entitys = entitys;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return companyName+"<-->"+relation;
	}


}
