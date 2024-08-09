package com.asiapeak.spring.downloader.dto;

import com.asiapeak.spring.downloader.function.ResponseHandlerConsumer;

import lombok.Data;

@Data
public class ResponseStream {
	ResponseHandlerConsumer consumer;
}
