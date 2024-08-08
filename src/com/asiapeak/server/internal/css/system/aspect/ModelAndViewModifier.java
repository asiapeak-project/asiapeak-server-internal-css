package com.asiapeak.server.internal.css.system.aspect;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.CustomerServiceApplication;
import com.asiapeak.server.internal.css.system.UserNameService;

@Aspect
@Component
public class ModelAndViewModifier {

	@Autowired
	UserNameService userNameService;

	@AfterReturning(pointcut = "execution(org.springframework.web.servlet.ModelAndView com.asiapeak.server.internal.css.functions.*.*.*(..))", returning = "modelAndView")
	public void afterReturningControllerMethod(ModelAndView modelAndView) {
		if (modelAndView != null) {

			String userName = userNameService.getCurrentUserName();

			if (StringUtils.isNoneBlank(userName)) {
				userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
			}
			modelAndView.addObject("USER_NAME", userName);
			// modelAndView.addObject("RESOURCE_UUID",
			// CustomerServiceApplication.RESOURCE_UUID); // TODO
			modelAndView.addObject("RESOURCE_UUID", UUID.randomUUID().toString());
		}
	}

}
