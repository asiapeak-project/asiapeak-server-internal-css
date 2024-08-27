package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DocumentInputDto {
	String category;
	String subject;
	String content;
	List<String> delFiles;
	List<MultipartFile> files;
}
