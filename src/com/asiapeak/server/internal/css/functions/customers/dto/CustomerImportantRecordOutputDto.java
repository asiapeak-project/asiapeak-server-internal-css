package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerImportantRecordOutputDto {
	Integer rowid;
	String record;
	Date udate;
	Boolean marked;
}
