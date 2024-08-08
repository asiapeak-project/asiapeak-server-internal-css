package com.asiapeak.server.internal.css.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table
@Entity
@Accessors(chain = true)
public class Contact {

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
	String email;

	@Type(type = "text")
	String mobilePhone;

	@Type(type = "text")
	String officePhone;

	@Type(type = "text")
	String position;

	@Type(type = "text")
	String memo;

	@Type(type = "text")
	String cuser;

	Date cdate;

	@Type(type = "text")
	String uuser;
	
	Date udate;
	
	@ManyToOne
	Customer customer;

}
