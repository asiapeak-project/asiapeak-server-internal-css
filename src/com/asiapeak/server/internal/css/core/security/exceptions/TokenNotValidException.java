package com.asiapeak.server.internal.css.core.security.exceptions;

@SuppressWarnings("serial")
public class TokenNotValidException extends RuntimeException {
	public TokenNotValidException(String message) {
		super(message);
	}
}
