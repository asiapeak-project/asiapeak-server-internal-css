package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DeploymentDto {
	Integer rowid;
	String subject;
	String infoColumns;
	String infoValues;
	Date udate;
	String uuser;
	
	Date cdate;
	String cuser;
}
