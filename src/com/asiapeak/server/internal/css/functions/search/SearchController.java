package com.asiapeak.server.internal.css.functions.search;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("search")
public class SearchController {

	@GetMapping
	public ModelAndView search() {
		return new ModelAndView("view/search/search");
	}
	
}
