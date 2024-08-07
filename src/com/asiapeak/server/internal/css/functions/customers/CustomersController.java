package com.asiapeak.server.internal.css.functions.customers;

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
import com.asiapeak.server.internal.css.dao.entity.Customer;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerContactDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerImportantRecordInputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerImportantRecordOutputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerInfoDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerInputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerOutputDto;

@Controller
@RequestMapping("customers")
public class CustomersController {

	@Autowired
	CustomersService customersService;

	@GetMapping
	public ModelAndView customers() {
		return new ModelAndView("view/customers/customers");
	}

	@ResponseBody
	@PostMapping("qryCustomers")
	public ResponseBean<List<CustomerOutputDto>> qryCustomers() {
		List<CustomerOutputDto> list = customersService.qryCustomers();
		return ResponseBean.success(list);
	}

	@GetMapping("newCustomer")
	public ModelAndView newCustomer() {
		return new ModelAndView("view/customers/newCustomer");
	}

	@ResponseBody
	@PostMapping("newCustomer")
	public ResponseBean<Boolean> newCustomer(@RequestBody CustomerInputDto dto) {
		String msg = customersService.newCustomer(dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@GetMapping("{rowid}")
	public ModelAndView customerTabs(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/customerTabs");

		Customer customer = customersService.findCustomer(rowid);

		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}

		return view;
	}

	@GetMapping("info/{rowid}")
	public ModelAndView customerInfo(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-info");

		Customer customer = customersService.findCustomer(rowid);

		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}

		return view;
	}

	@ResponseBody
	@PostMapping("qryInfo/{rowid}")
	public ResponseBean<CustomerInfoDto> qryInfo(@PathVariable("rowid") Integer rowid) {
		CustomerInfoDto dto = customersService.qryInfo(rowid);
		return ResponseBean.success(dto);
	}

	@GetMapping("editInfo/{rowid}")
	public ModelAndView editInfo(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-edit-info");

		Customer customer = customersService.findCustomer(rowid);

		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}

		return view;
	}

	@ResponseBody
	@PostMapping("editInfo/{rowid}")
	public ResponseBean<Boolean> editInfo(@PathVariable("rowid") Integer rowid, @RequestBody CustomerInfoDto dto) {
		String msg = customersService.editInfo(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@GetMapping("contact/{rowid}")
	public ModelAndView customerContact(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-contact");

		Customer customer = customersService.findCustomer(rowid);

		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}

		return view;
	}

	@ResponseBody
	@PostMapping("qryContact/{rowid}")
	public ResponseBean<List<CustomerContactDto>> qryContact(@PathVariable("rowid") Integer rowid) {
		List<CustomerContactDto> list = customersService.qryContact(rowid);
		return ResponseBean.success(list);
	}

	@GetMapping("createContact/{rowid}")
	public ModelAndView createContact(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-create-contact");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@ResponseBody
	@PostMapping("createContact/{rowid}")
	public ResponseBean<Boolean> createContact(@PathVariable("rowid") Integer rowid, @RequestBody CustomerContactDto dto) {
		String msg = customersService.createContact(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("qryContactData/{rowid}")
	public ResponseBean<CustomerContactDto> qryContactData(@PathVariable("rowid") Integer rowid) {
		CustomerContactDto dto = customersService.qryContactData(rowid);
		return ResponseBean.success(dto);
	}

	@GetMapping("editContactData/{rowid}")
	public ModelAndView editContactData(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-edit-contact");
		view.addObject("rowid", rowid);
		return view;
	}

	@ResponseBody
	@PostMapping("editContactData")
	public ResponseBean<Boolean> editContactData(@RequestBody CustomerContactDto dto) {
		String msg = customersService.editContactData(dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("delContact/{rowid}")
	public ResponseBean<Boolean> delContact(@PathVariable("rowid") Integer rowid) {
		String msg = customersService.delContact(rowid);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@GetMapping("importantRecord/{rowid}")
	public ModelAndView importantRecord(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-importantRecord");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@ResponseBody
	@PostMapping("qryImportantRecords/{rowid}")
	public ResponseBean<List<CustomerImportantRecordOutputDto>> qryImportantRecords(@PathVariable("rowid") Integer rowid) {
		List<CustomerImportantRecordOutputDto> list = customersService.qryImportantRecords(rowid);
		return ResponseBean.success(list);
	}
	
	@GetMapping("createImportantRecord/{rowid}")
	public ModelAndView createImportantRecord(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-create-importantRecord");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@ResponseBody
	@PostMapping("createImportantRecord/{rowid}")
	public ResponseBean<Boolean> createImportantRecord(@PathVariable("rowid") Integer rowid, @RequestBody CustomerImportantRecordInputDto dto) {
		String msg = customersService.createImportantRecord(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("updateImportantRecord/{rowid}/{marked}")
	public ResponseBean<Boolean> updateImportantRecord(@PathVariable("rowid") Integer rowid, @PathVariable("marked") Boolean marked) {
		String msg = customersService.updateImportantRecord(rowid, marked);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("delImportantRecord/{rowid}")
	public ResponseBean<Boolean> delImportantRecord(@PathVariable("rowid") Integer rowid) {
		String msg = customersService.delImportantRecord(rowid);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@GetMapping("product/{rowid}")
	public ModelAndView customerProduct(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-product");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@GetMapping("contract/{rowid}")
	public ModelAndView customerContract(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-contract");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@GetMapping("deployment/{rowid}")
	public ModelAndView customerDeployment(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-deployment");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@GetMapping("document/{rowid}")
	public ModelAndView customerDocument(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-document");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@GetMapping("serviceRecord/{rowid}")
	public ModelAndView customerServiceRecord(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-serviceRecord");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

	@GetMapping("contactRecord/{rowid}")
	public ModelAndView customerContactRecord(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/tabs/customer-contactRecord");
		Customer customer = customersService.findCustomer(rowid);
		if (customer == null) {
			view.addObject("dname", "不存在");
			view.addObject("rowid", "-1");
		} else {
			view.addObject("dname", customer.getDname());
			view.addObject("rowid", customer.getRowid());
		}
		return view;
	}

}
