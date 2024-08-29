package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ServiceRecordHandleInputDto {

	String handlePerson;

	String handleContent;

	Date handleDate;
	
	String handleResult;

	List<String> delFiles;

	List<MultipartFile> files;
}
