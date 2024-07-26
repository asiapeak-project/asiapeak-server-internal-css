package com.asiapeak.server.internal.css.functions.customers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("customers")
public class CustomersController {

	@GetMapping
	public ModelAndView customers() {
		return new ModelAndView("view/customers/customers");
	}

}
