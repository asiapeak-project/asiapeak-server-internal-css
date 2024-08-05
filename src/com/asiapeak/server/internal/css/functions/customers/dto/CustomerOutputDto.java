package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerOutputDto {

	Integer rowid;
	
	String dname;
	
	String cname;
	
	String ename;
	
	Date udate;
}
