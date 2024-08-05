package com.asiapeak.server.internal.css.core.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	@Autowired
	HttpServletRequest request;

	public String getCurrentUserName() {
		return (String) request.getSession().getAttribute(SecurityConfig.USER_SESSION_KEY);
	}

	public void setCurrentUserName(String userName) {
		request.getSession().setAttribute(SecurityConfig.USER_SESSION_KEY, userName);
	}

}
