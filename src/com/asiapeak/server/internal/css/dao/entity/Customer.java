package com.asiapeak.server.internal.css.dao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table
@Entity
@Accessors(chain = true)
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Integer rowid;

	@Type(type = "text")
	String dname;

	@Type(type = "text")
	String cname;

	@Type(type = "text")
	String ename;

	@Type(type = "text")
	String phone;

	@Type(type = "text")
	String address;

	@Type(type = "text")
	String website;

	@Type(type = "text")
	String unumber;

	@Type(type = "text")
	String industry;

	@Type(type = "text")
	String memo;

	@Type(type = "text")
	String cuser;

	Date cdate;

	@Type(type = "text")
	String uuser;

	Date udate;

	@Type(type = "text")
	String detailUuser;

	Date detailUdate;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	List<Contact> contacts;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	List<Product> products;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	List<Deployment> deployments;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	List<Document> documents;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	List<ServiceRecord> serviceRecords;

}
