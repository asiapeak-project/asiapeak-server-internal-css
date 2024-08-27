package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ServiceRecordOutputDto {

	Integer rowid;

	Integer customerRowid;
	
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
	
	List<AttachementDto> files;

}
