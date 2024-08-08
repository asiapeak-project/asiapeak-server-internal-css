package com.asiapeak.server.internal.css.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiapeak.server.internal.css.core.security.SecurityConfig;

@Service
public class UserNameService {

	@Autowired
	HttpServletRequest request;

	public String getCurrentUserName() {
		return (String) request.getSession().getAttribute(SecurityConfig.USER_SESSION_KEY);
	}

}
