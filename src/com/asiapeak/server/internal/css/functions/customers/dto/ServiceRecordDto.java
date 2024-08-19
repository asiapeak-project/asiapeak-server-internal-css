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

	String handleResult;

	Date serviceDate;

	String cuser;

	Date cdate;

	String uuser;

	Date udate;

}
