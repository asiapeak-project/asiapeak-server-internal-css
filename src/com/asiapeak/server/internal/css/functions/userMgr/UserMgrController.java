package com.asiapeak.server.internal.css.functions.userMgr;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("userMgr")
@Secured("ROLE_USERMGR_VIEW")
public class UserMgrController {

	@GetMapping
	public ModelAndView userMgr() {
		return new ModelAndView("view/userMgr/userMgr");
	}

	
}
