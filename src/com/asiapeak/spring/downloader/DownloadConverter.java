package com.asiapeak.spring.downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.asiapeak.spring.downloader.abstracts.AbstractDownloadHandler;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DownloadConverter extends AbstractHttpMessageConverter<Object> {

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	List<AbstractDownloadHandler<?>> handlers = new ArrayList<>();

	@Autowired
	ApplicationContext applicationContext;

	@PostConstruct
	private void doInit() {
		applicationContext.getBeansOfType(AbstractDownloadHandler.class).forEach((key, bean) -> {
			handlers.add(bean);
		});
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		for (AbstractDownloadHandler<?> converte : handlers) {
			if (converte.canRead(clazz, mediaType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		for (HttpMessageConverter<?> converte : handlers) {
			if (converte.canWrite(clazz, mediaType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		for (AbstractDownloadHandler<?> converter : handlers) {
			if (converter.supportType() == clazz) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new HttpMessageNotReadableException("Type Not Readable", inputMessage);
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		for (AbstractDownloadHandler<?> converter : handlers) {
			if (converter.supportType() == object.getClass()) {
				converter.doWrite(object, request, response);
				break;
			}
		}
	}

}
