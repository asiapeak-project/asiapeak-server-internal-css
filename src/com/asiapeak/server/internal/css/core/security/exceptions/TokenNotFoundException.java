package com.asiapeak.server.internal.css.core.security.exceptions;

@SuppressWarnings("serial")
public class TokenNotFoundException extends RuntimeException {

	boolean shouldLog = true;

	public TokenNotFoundException(String message) {
		super(message);
	}

	public TokenNotFoundException(String message, boolean shouldLog) {
		super(message);
		this.shouldLog = shouldLog;
	}

	public boolean needToLog() {
		return this.shouldLog;
	}
}
