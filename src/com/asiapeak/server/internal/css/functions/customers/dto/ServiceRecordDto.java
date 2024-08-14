package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ServiceRecordDto {

	Integer rowid;

	String subject;

	String type;

	String contactPerson;

	String serviceContent;

	Integer handleResult;

	String handlePerson;

	String handleContent;

	Date serviceDate;

	Date handleDate;

	String cuser;
	Date cdate;

	String uuser;
	Date udate;

}
