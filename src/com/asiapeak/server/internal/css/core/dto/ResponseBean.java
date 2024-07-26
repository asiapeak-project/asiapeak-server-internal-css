package com.asiapeak.server.internal.css.core.dto;

import lombok.Data;

@Data
public class ResponseBean<T> {

	boolean succeeded;

	T data;

	String message;

	public static <T> ResponseBean<T> success(T data) {
		ResponseBean<T> rb = new ResponseBean<>();
		rb.succeeded = true;
		rb.data = data;
		return rb;
	}

	public static <T> ResponseBean<T> error(T data) {
		ResponseBean<T> rb = new ResponseBean<>();
		rb.succeeded = false;
		rb.data = data;
		return rb;
	}

	public ResponseBean<T> message(String message) {
		this.message = message;
		return this;
	}

}
