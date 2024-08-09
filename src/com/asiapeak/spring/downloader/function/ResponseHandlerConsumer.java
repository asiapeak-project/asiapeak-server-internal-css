package com.asiapeak.spring.downloader.function;

import java.io.IOException;

import com.asiapeak.spring.downloader.vo.ResponseHandler;

@FunctionalInterface
public interface ResponseHandlerConsumer {
	void accept(ResponseHandler responseHandler) throws IOException;
}
