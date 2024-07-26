package com.asiapeak.server.internal.css.system.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.core.security.SecurityService;
import com.asiapeak.server.internal.css.core.security.SecurityUtils;

@Aspect
@Component
public class ModelAndViewModifier {

	@Autowired
	SecurityService securityService;

	@AfterReturning(pointcut = "execution(org.springframework.web.servlet.ModelAndView com.asiapeak.server.internal.css.functions.*.*.*(..))", returning = "modelAndView")
	public void afterReturningControllerMethod(ModelAndView modelAndView) {
		if (modelAndView != null) {
			String userName = securityService.currentUser.get();
			if (StringUtils.isNoneBlank(userName)) {
				userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
			}
			modelAndView.addObject("USER_NAME", userName);
		}
	}

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@AfterReturning(pointcut = "execution(* com.asiapeak.server.internal.css.functions.*.*.*(..))")
	public void refreshToken() throws Exception {
		String userName = securityService.currentUser.get();
		String newJWT = securityService.createJWT(userName, request);
		SecurityUtils.setCookieValue(request, response, HttpHeaders.AUTHORIZATION, newJWT);
	}

}
