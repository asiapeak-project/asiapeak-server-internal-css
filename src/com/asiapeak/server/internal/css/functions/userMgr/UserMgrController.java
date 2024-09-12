package com.asiapeak.server.internal.css.functions.userMgr;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.core.dto.ResponseBean;
import com.asiapeak.server.internal.css.functions.userMgr.dto.CreateUserInputDto;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UserAuthInputDto;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UserAuthOutputDto;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UserChangePwdInputDto;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UsersOutputDto;

@Controller
@RequestMapping("userMgr")
@Secured("ROLE_USERMGR_FUNCTION")
public class UserMgrController {

	@Autowired
	UserMgrService userMgrService;

	@GetMapping
	public ModelAndView userMgr() {
		return new ModelAndView("view/userMgr/userMgr");
	}

	@ResponseBody
	@PostMapping("qryUsers")
	public ResponseBean<List<UsersOutputDto>> qryUsers() {
		List<UsersOutputDto> list = userMgrService.qryUsers();
		return ResponseBean.success(list);
	}

	@GetMapping("createUser")
	public ModelAndView createUser() {
		return new ModelAndView("view/userMgr/dialogs/userMgr-create");
	}

	@ResponseBody
	@PostMapping("createUser")
	public ResponseBean<Boolean> createUser(@RequestBody CreateUserInputDto dto) {
		String msg = userMgrService.createUser(dto.getAccount(), dto.getPassword());
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(false).message(msg);
		}
	}

	@GetMapping("auth/{rowid}")
	public ModelAndView auth(@PathVariable Integer rowid) {
		return new ModelAndView("view/userMgr/dialogs/userMgr-auth").addObject("rowid", rowid);
	}

	@ResponseBody
	@PostMapping("qryAuth/{rowid}")
	public ResponseBean<List<UserAuthOutputDto>> qryAuth(@PathVariable Integer rowid) {
		List<UserAuthOutputDto> list = userMgrService.qryAuth(rowid);
		return ResponseBean.success(list);
	}

	@ResponseBody
	@PostMapping("editAuth/{rowid}")
	public ResponseBean<Boolean> editAuth(@PathVariable Integer rowid, @RequestBody UserAuthInputDto dto) {
		String msg = userMgrService.editAuth(rowid, dto.getAuths());
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(false).message(msg);
		}
	}

	@GetMapping("chpwd/{rowid}")
	public ModelAndView chpwd(@PathVariable Integer rowid) {
		return new ModelAndView("view/userMgr/dialogs/userMgr-chpwd").addObject("rowid", rowid);
	}

	@ResponseBody
	@PostMapping("chpwd/{rowid}")
	public ResponseBean<Boolean> chpwd(@PathVariable Integer rowid, @RequestBody UserChangePwdInputDto dto) {
		String msg = userMgrService.chpwd(rowid, dto.getPassword());
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(false).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("delUser/{rowid}")
	public ResponseBean<Boolean> delUser(@PathVariable Integer rowid) {
		String msg = userMgrService.delUser(rowid);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(false).message(msg);
		}
	}

}
