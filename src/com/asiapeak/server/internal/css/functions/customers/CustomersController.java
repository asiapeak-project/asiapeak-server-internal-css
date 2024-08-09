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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.asiapeak.server.internal.css.core.dto.ResponseBean;
import com.asiapeak.server.internal.css.dao.entity.Customer;
import com.asiapeak.server.internal.css.functions.customers.dto.ContactDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerDto;
import com.asiapeak.server.internal.css.functions.customers.dto.DeploymentDto;
import com.asiapeak.server.internal.css.functions.customers.dto.DeploymentOutptuDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ImportantRecordDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ProductDto;

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
	public ResponseBean<List<CustomerDto>> qryCustomers() {
		List<CustomerDto> list = customersService.qryCustomers();
		return ResponseBean.success(list);
	}

	@GetMapping("createCustomer")
	public ModelAndView createCustomer() {
		return new ModelAndView("view/customers/createCustomer");
	}

	@ResponseBody
	@PostMapping("createCustomer")
	public ResponseBean<Boolean> createCustomer(@RequestBody CustomerDto dto) {
		String msg = customersService.createCustomer(dto);
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
	public ResponseBean<CustomerDto> qryInfo(@PathVariable("rowid") Integer rowid) {
		CustomerDto dto = customersService.qryInfo(rowid);
		return ResponseBean.success(dto);
	}

	@GetMapping("editInfo/{rowid}")
	public ModelAndView editInfo(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-info-edit");

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
	public ResponseBean<Boolean> editInfo(@PathVariable("rowid") Integer rowid, @RequestBody CustomerDto dto) {
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
	@PostMapping("qryContacts/{rowid}")
	public ResponseBean<List<ContactDto>> qryContacts(@PathVariable("rowid") Integer rowid) {
		List<ContactDto> list = customersService.qryContacts(rowid);
		return ResponseBean.success(list);
	}

	@GetMapping("createContact/{rowid}")
	public ModelAndView createContact(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-contact-create");
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
	public ResponseBean<Boolean> createContact(@PathVariable("rowid") Integer rowid, @RequestBody ContactDto dto) {
		String msg = customersService.createContact(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("qryContact/{rowid}")
	public ResponseBean<ContactDto> qryContact(@PathVariable("rowid") Integer rowid) {
		ContactDto dto = customersService.qryContact(rowid);
		return ResponseBean.success(dto);
	}

	@GetMapping("editContact/{rowid}")
	public ModelAndView editContact(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-contact-edit");
		view.addObject("rowid", rowid);
		return view;
	}

	@ResponseBody
	@PostMapping("editContact")
	public ResponseBean<Boolean> editContact(@RequestBody ContactDto dto) {
		String msg = customersService.editContact(dto);
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
	public ResponseBean<List<ImportantRecordDto>> qryImportantRecords(@PathVariable("rowid") Integer rowid) {
		List<ImportantRecordDto> list = customersService.qryImportantRecords(rowid);
		return ResponseBean.success(list);
	}

	@GetMapping("createImportantRecord/{rowid}")
	public ModelAndView createImportantRecord(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-importantRecord-create");
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
	public ResponseBean<Boolean> createImportantRecord(@PathVariable("rowid") Integer rowid, @RequestBody ImportantRecordDto dto) {
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

	@ResponseBody
	@PostMapping("qryProducts/{rowid}")
	public ResponseBean<List<ProductDto>> qryProducts(@PathVariable("rowid") Integer rowid) {
		List<ProductDto> list = customersService.qryProducts(rowid);
		return ResponseBean.success(list);
	}

	@GetMapping("createProduct/{rowid}")
	public ModelAndView createProduct(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-product-create");
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
	@PostMapping("createProduct/{rowid}")
	public ResponseBean<Boolean> createProduct(@PathVariable("rowid") Integer rowid, @RequestBody ProductDto dto) {
		String msg = customersService.createProduct(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("delProduct/{rowid}")
	public ResponseBean<Boolean> delProduct(@PathVariable("rowid") Integer rowid) {
		String msg = customersService.delProduct(rowid);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@GetMapping("editProduct/{rowid}")
	public ModelAndView editProduct(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-product-edit");
		view.addObject("rowid", rowid);
		return view;
	}

	@ResponseBody
	@PostMapping("qryProduct/{rowid}")
	public ResponseBean<ProductDto> qryProduct(@PathVariable("rowid") Integer rowid) {
		ProductDto dto = customersService.qryProduct(rowid);
		return ResponseBean.success(dto);
	}

	@ResponseBody
	@PostMapping("editProduct")
	public ResponseBean<Boolean> editProduct(@RequestBody ProductDto dto) {
		String msg = customersService.editProduct(dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
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

	@ResponseBody
	@PostMapping("qryDeployments/{rowid}")
	public ResponseBean<List<DeploymentDto>> qryDeployments(@PathVariable("rowid") Integer rowid) {
		List<DeploymentDto> list = customersService.qryDeployments(rowid);
		return ResponseBean.success(list);
	}

	@GetMapping("createDeployment/{rowid}")
	public ModelAndView createDeployment(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-deployment-create");
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
	@PostMapping("createDeployment/{rowid}")
	public ResponseBean<Boolean> createDeployment(@PathVariable("rowid") Integer rowid, @RequestBody DeploymentDto dto) {
		String msg = customersService.createDeployment(rowid, dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@ResponseBody
	@PostMapping("delDeployment/{rowid}")
	public ResponseBean<Boolean> delDeployment(@PathVariable("rowid") Integer rowid) {
		String msg = customersService.delDeployment(rowid);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
	}

	@GetMapping("editDeployment/{rowid}")
	public ModelAndView editDeployment(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-deployment-edit");
		view.addObject("rowid", rowid);
		return view;
	}

	@ResponseBody
	@PostMapping("qryDeployment/{rowid}")
	public ResponseBean<DeploymentDto> qryDeployment(@PathVariable("rowid") Integer rowid) {
		DeploymentDto dto = customersService.qryDeployment(rowid);
		return ResponseBean.success(dto);
	}

	@ResponseBody
	@PostMapping("editDeployment")
	public ResponseBean<Boolean> editDeployment(@RequestBody ProductDto dto) {
		String msg = customersService.editDeployment(dto);
		if (StringUtils.isBlank(msg)) {
			return ResponseBean.success(true);
		} else {
			return ResponseBean.error(true).message(msg);
		}
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
	
	@ResponseBody
	@PostMapping("qryDocuments/{rowid}")
	public ResponseBean<List<DeploymentOutptuDto>> qryDocuments(@PathVariable("rowid") Integer rowid) {
		List<DeploymentOutptuDto> list = customersService.qryDocuments(rowid);
		return ResponseBean.success(list);
	}

	@ResponseBody
	@PostMapping("qryDocumentCatetories")
	public ResponseBean<List<String>> qryDocumentCatetories() {
		List<String> list = customersService.qryDocumentCatetories();
		return ResponseBean.success(list);
	}

	@GetMapping("createDocument/{rowid}")
	public ModelAndView createDocument(@PathVariable("rowid") Integer rowid) {
		ModelAndView view = new ModelAndView("view/customers/dialogs/customer-document-create");
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
	@PostMapping(path = "createDocument/{rowid}", consumes = "multipart/form-data")
	public ResponseBean<Boolean> createDocument(@PathVariable("rowid") Integer rowid, @RequestParam("category") String category, @RequestParam("subject") String subject, @RequestParam("content") String content, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws Exception {
		customersService.createDocument(rowid, category, subject, content, files);
		return ResponseBean.success(true);
	}

	////
	////
	////
	////////
	////////
	////////
	////

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
