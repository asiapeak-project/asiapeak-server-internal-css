package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DeploymentOutptuDto {

	Integer rowid;

	String category;

	String subject;

	Integer attachments;
	
	Date udate;
	String uuser;
	
	Date cdate;
	String cuser;

}
