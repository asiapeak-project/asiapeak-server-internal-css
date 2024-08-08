package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ContactDto {

	Integer rowid;

	String dname;

	String cname;

	String ename;

	String email;

	String mobilePhone;

	String officePhone;

	String position;

	String memo;

	Date udate;
	String uuser;
	
	Date cdate;
	String cuser;
	
}
