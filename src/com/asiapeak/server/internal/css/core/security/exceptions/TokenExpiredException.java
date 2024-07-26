package com.asiapeak.server.internal.css.core.security.exceptions;

@SuppressWarnings("serial")
public class TokenExpiredException extends RuntimeException {
	public TokenExpiredException(String message) {
		super(message);
	}
}
