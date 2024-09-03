package com.asiapeak.server.internal.css.functions.templates;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.core.dto.ResponseBean;
import com.asiapeak.server.internal.css.functions.templates.dto.TemplatesInputDto;
import com.asiapeak.server.internal.css.functions.templates.dto.TemplatesOutputDto;

@Controller
@RequestMapping("templates")
public class TemplatesController {

	@Autowired
	TemplatesService templatesService;

	@GetMapping
	public ModelAndView templates() {
		return new ModelAndView("view/templates/templates");
	}

	@GetMapping("edit/{rowid}")
	public ModelAndView edit(@PathVariable("rowid") Integer rowid) {
		ModelAndView modelAndView = new ModelAndView("view/templates/templates-edit");
		modelAndView.addObject("rowid", rowid);
		return modelAndView;
	}

	@GetMapping("create")
	public ModelAndView create() {
		return new ModelAndView("view/templates/templates-create");
	}

	@ResponseBody
	@PostMapping("list")
	public ResponseBean<List<TemplatesOutputDto>> list() {
		List<TemplatesOutputDto> list = templatesService.list();
		return ResponseBean.success(list);
	}

	@ResponseBody
	@PostMapping("getTemplate/{rowid}")
	public ResponseBean<TemplatesOutputDto> getTemplate(@PathVariable("rowid") Integer rowid) {
		TemplatesOutputDto dto = templatesService.getTemplate(rowid);
		return ResponseBean.success(dto);
	}

	@ResponseBody
	@PostMapping("create")
	public ResponseBean<Boolean> create(@RequestBody TemplatesInputDto dto) {
		String msg = templatesService.create(dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.success(false).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("update/{rowid}")
	public ResponseBean<Boolean> update(@PathVariable("rowid") Integer rowid, @RequestBody TemplatesInputDto dto) {
		String msg = templatesService.update(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.success(false).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("delete/{rowid}")
	public ResponseBean<Boolean> delete(@PathVariable("rowid") Integer rowid) {
		String msg = templatesService.delete(rowid);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.success(false).message(msg);
		}
	}

}
