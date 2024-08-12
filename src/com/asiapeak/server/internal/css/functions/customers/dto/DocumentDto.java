package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DocumentDto {

	Integer parentRowid;
	Integer rowid;
	String category;
	String subject;
	String content;
	List<DocumentAttachementDto> files;

	String cuser;
	Date cdate;
	String uuser;
	Date udate;
}
