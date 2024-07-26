package com.asiapeak.server.internal.css.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.core.dto.ResponseBean;
import com.asiapeak.server.internal.css.core.security.SecurityService;
import com.asiapeak.server.internal.css.core.security.SecurityUtils;
import com.asiapeak.server.internal.css.system.dto.LoginInputDto;

@Controller
@RequestMapping
public class SystemController {

	@Autowired
	SecurityService securityService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@GetMapping
	public ModelAndView index() {
		ModelAndView view;
		if (StringUtils.isBlank(securityService.currentUser.get())) {
			view = new ModelAndView("view/login");
		} else {
			view = new ModelAndView("view/customers/customers");
		}
		return view;
	}

	@PostMapping("login")
	@ResponseBody
	public ResponseBean<Boolean> login(@RequestBody LoginInputDto dto) throws Exception {
		String jwt = securityService.createJWT(dto.getName(), request);
		SecurityUtils.setCookieValue(request, response, HttpHeaders.AUTHORIZATION, jwt);
		// return ResponseBean.success(false).message("登入失敗");
		return ResponseBean.success(true);
	}

	@PostMapping("logout")
	@ResponseBody
	public ResponseBean<Boolean> logout() {
		SecurityUtils.setCookieValue(request, response, HttpHeaders.AUTHORIZATION, null);
		return ResponseBean.success(true);
	}

}
