package com.asiapeak.spring.downloader.vo;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseHandler {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public void setResponseHeader(HttpHeaders httpHeader, String value) {
		response.setHeader(httpHeader.toString(), value);
	}

	public void setResponseDateHeader(HttpHeaders httpHeader, long value) {
		response.setDateHeader(httpHeader.toString(), value);
	}

	public void setStatue(HttpStatus status) {
		response.setStatus(status.value());
	}

	public String getRequestHeader(HttpHeaders httpHeader) {
		return request.getHeader(httpHeader.toString());
	}

	public long getRequestDateHeader(HttpHeaders httpHeader) {
		return request.getDateHeader(httpHeader.toString());
	}

	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

}
