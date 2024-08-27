package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DocumentOutputDto {

	Integer parentRowid;
	Integer rowid;
	String category;
	String subject;
	String content;
	List<AttachementDto> files;

	String cuser;
	Date cdate;
	String uuser;
	Date udate;
}
