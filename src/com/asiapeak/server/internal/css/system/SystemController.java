package com.asiapeak.server.internal.css.system;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.core.dto.ResponseBean;
import com.asiapeak.server.internal.css.core.user.UserAuthService;
import com.asiapeak.server.internal.css.system.dto.ChangePasswordInputDto;
import com.asiapeak.server.internal.css.system.dto.LoginInputDto;

@Controller
@RequestMapping
public class SystemController {

	@Autowired
	UserAuthService userAuthService;

	@GetMapping
	public ModelAndView index() {
		if (!userAuthService.isAdminCreated()) {
			return new ModelAndView("view/createAdmin");
		} else if (StringUtils.isBlank(userAuthService.getCurrentUserName())) {
			return new ModelAndView("view/login");
		} else {
			return new ModelAndView("view/home");
		}
	}

	@PostMapping("login")
	@ResponseBody
	public ResponseBean<Boolean> login(@RequestBody LoginInputDto dto) throws Exception {

		boolean result = userAuthService.doLogin(dto.getName(), dto.getPassword());

		if (result) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.success(false).message("登入失敗");
		}

	}

	@PostMapping("createAdmin")
	@ResponseBody
	public ResponseBean<Boolean> createAdmin(@RequestBody LoginInputDto dto) throws Exception {

		if (userAuthService.isAdminCreated()) {
			return ResponseBean.success(false).message("管理者已存在");
		}

		boolean result = userAuthService.createAdmin(dto.getName(), dto.getPassword());

		if (result) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.success(false).message("建立管理者失敗");
		}

	}

	@PostMapping("logout")
	@ResponseBody
	public ResponseBean<Boolean> logout() {
		userAuthService.doLogout();
		return ResponseBean.success(true);
	}

	@GetMapping("accessDenied")
	public ModelAndView accessDenied() {
		return new ModelAndView("view/accessDenied");
	}

	@GetMapping("changePassword")
	public ModelAndView changePassword() {
		return new ModelAndView("view/changePassword");
	}

	@ResponseBody
	@PostMapping("changePassword")
	public ResponseBean<Boolean> changePassword(@RequestBody ChangePasswordInputDto dto) {

		String msg = userAuthService.changeCurrentUserPassword(dto.getOldPassword(), dto.getNewPassword());

		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.success(false).message(msg);
		}
	}

}
