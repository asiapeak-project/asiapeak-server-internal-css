package com.asiapeak.spring.downloader.dto;

import org.springframework.http.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBytes {
	String fileName;
	byte[] bytes;
	long lastModified;
	MediaType mediaType;
}
