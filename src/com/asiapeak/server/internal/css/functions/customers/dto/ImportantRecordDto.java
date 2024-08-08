package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ImportantRecordDto {
	Integer rowid;
	String record;
	Boolean marked;
	Date udate;
	String uuser;
	
	Date cdate;
	String cuser;
	
}
