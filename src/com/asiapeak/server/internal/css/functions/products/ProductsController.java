package com.asiapeak.server.internal.css.functions.products;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("products")
public class ProductsController {

	@GetMapping
	public ModelAndView products() {
		return new ModelAndView("view/products/products");
	}
	
}
