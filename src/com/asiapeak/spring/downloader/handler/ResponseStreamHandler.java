package com.asiapeak.spring.downloader.handler;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asiapeak.spring.downloader.abstracts.AbstractDownloadHandler;
import com.asiapeak.spring.downloader.dto.ResponseStream;
import com.asiapeak.spring.downloader.function.ResponseHandlerConsumer;
import com.asiapeak.spring.downloader.vo.ResponseHandler;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResponseStreamHandler extends AbstractDownloadHandler<ResponseStream> {

	@Override
	public Class<?> supportType() {
		return ResponseStream.class;
	}

	@Override
	protected void write(ResponseStream responseStream, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResponseHandler responseHandler = new ResponseHandler(request, response);
		ResponseHandlerConsumer consumer = responseStream.getConsumer();
		Objects.requireNonNull(consumer, "consumer is null");
		consumer.accept(responseHandler);
	}

}
