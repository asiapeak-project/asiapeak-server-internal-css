package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ServiceRecordHandleDto {

	Integer rowid;

	String handlePerson;

	String handleContent;

	Date handleDate;

	String cuser;

	Date cdate;

	String uuser;

	Date udate;

}
