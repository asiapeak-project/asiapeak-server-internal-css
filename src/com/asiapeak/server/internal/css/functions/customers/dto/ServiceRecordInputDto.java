package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ServiceRecordInputDto {
	String subject;
	String type;
	String contactPerson;
	String serviceContent;
	String handleResult;
	Date serviceDate;
	List<String> delFiles;
	List<MultipartFile> files;
}
