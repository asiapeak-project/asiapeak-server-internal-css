package com.asiapeak.server.internal.css.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiapeak.server.internal.css.core.user.UserAuthService;

@Component
public class ViewAuth {

	@Autowired
	UserAuthService userAuthService;

	public boolean allowed(String name) {
		return userAuthService.getCurrentAuthRoles().contains(name);
	}
}
