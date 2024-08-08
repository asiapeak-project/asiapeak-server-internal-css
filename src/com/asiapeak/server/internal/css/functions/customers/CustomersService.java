package com.asiapeak.server.internal.css.functions.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.entity.Contact;
import com.asiapeak.server.internal.css.dao.entity.Customer;
import com.asiapeak.server.internal.css.dao.entity.ImportantRecord;
import com.asiapeak.server.internal.css.dao.entity.Product;
import com.asiapeak.server.internal.css.dao.repo.ContactRepo;
import com.asiapeak.server.internal.css.dao.repo.CustomerRepo;
import com.asiapeak.server.internal.css.dao.repo.ImportantRecordRepo;
import com.asiapeak.server.internal.css.dao.repo.ProductRepo;
import com.asiapeak.server.internal.css.functions.customers.dto.ContactDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ProductDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ImportantRecordDto;
import com.asiapeak.server.internal.css.system.UserNameService;

@Service
public class CustomersService {

	@Autowired
	UserNameService userNameService;

	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	ContactRepo contactRepo;

	@Autowired
	ImportantRecordRepo importantRecordRepo;

	@Autowired
	ProductRepo productRepo;

	@Transactional
	public List<CustomerDto> qryCustomers() {
		return customerRepo.findAll().stream().map(c -> {
			CustomerDto dto = new CustomerDto();
			dto.setRowid(c.getRowid());
			dto.setDname(c.getDname());
			dto.setCname(c.getCname());
			dto.setEname(c.getEname());
			dto.setDetailUdate(c.getDetailUdate());
			dto.setDetailUuser(c.getDetailUuser());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createCustomer(CustomerDto dto) {

		Customer customer = new Customer();
		customer.setDname(dto.getDname());
		customer.setCname(dto.getCname());
		customer.setEname(dto.getEname());
		customer.setIndustry(dto.getIndustry());
		customer.setMemo(dto.getMemo());
		customer.setPhone(dto.getPhone());
		customer.setUnumber(dto.getUnumber());
		customer.setWebsite(dto.getWebsite());
		customer.setAddress(dto.getAddress());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();

		customer.setUdate(now);
		customer.setUuser(user);
		customer.setCdate(now);
		customer.setCuser(user);
		customer.setDetailUdate(now);
		customer.setDetailUuser(user);

		customerRepo.saveAndFlush(customer);

		return null;
	}

	@Transactional
	public Customer findCustomer(Integer rowid) {
		return customerRepo.findById(rowid).orElse(null);
	}

	@Transactional
	public CustomerDto qryInfo(Integer rowid) {

		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return null;
		}

		CustomerDto dto = new CustomerDto();
		dto.setDname(customer.getDname());
		dto.setCname(customer.getCname());
		dto.setEname(customer.getEname());
		dto.setIndustry(customer.getIndustry());
		dto.setMemo(customer.getMemo());
		dto.setPhone(customer.getPhone());
		dto.setUnumber(customer.getUnumber());
		dto.setWebsite(customer.getWebsite());
		dto.setAddress(customer.getAddress());

		dto.setUdate(customer.getUdate());
		dto.setUuser(customer.getUuser());
		dto.setCdate(customer.getCdate());
		dto.setCuser(customer.getCuser());

		return dto;
	}

	@Transactional
	public List<ContactDto> qryContacts(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getContacts().stream().map(dao -> {
			ContactDto dto = new ContactDto();
			dto.setRowid(dao.getRowid());
			dto.setDname(dao.getDname());
			dto.setCname(dao.getCname());
			dto.setEname(dao.getEname());
			dto.setEmail(dao.getEmail());
			dto.setMobilePhone(dao.getMobilePhone());
			dto.setOfficePhone(dao.getOfficePhone());
			dto.setPosition(dao.getPosition());
			dto.setMemo(dao.getMemo());
			dto.setUdate(dao.getUdate());
			dto.setUuser(dao.getUuser());
			dto.setCdate(dao.getCdate());
			dto.setCuser(dao.getCuser());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createContact(Integer rowid, ContactDto dto) {

		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return "客戶不存在！";
		}

		Contact dao = new Contact();
		dao.setDname(dto.getDname());
		dao.setCname(dto.getCname());
		dao.setEname(dto.getEname());
		dao.setEmail(dto.getEmail());
		dao.setMobilePhone(dto.getMobilePhone());
		dao.setOfficePhone(dto.getOfficePhone());
		dao.setPosition(dto.getPosition());
		dao.setMemo(dto.getMemo());
		dao.setCustomer(customer);

		String user = userNameService.getCurrentUserName();
		Date now = new Date();

		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		contactRepo.save(dao);
		customerRepo.updateDetailTime(rowid, user);

		return null;
	}

	@Transactional
	public String delContact(Integer rowid) {

		Contact contact = contactRepo.findById(rowid).orElse(null);
		if (contact == null) {
			return "聯絡人不存在";
		}

		customerRepo.updateDetailTime(contact.getCustomer().getRowid(), userNameService.getCurrentUserName());
		contactRepo.delete(contact);

		return null;
	}

	@Transactional
	public String editInfo(Integer rowid, CustomerDto dto) {
		Customer dao = customerRepo.findById(rowid).orElse(null);

		if (dao == null) {
			return "客戶資訊不存在";
		}

		dao.setDname(dto.getDname());
		dao.setCname(dto.getCname());
		dao.setEname(dto.getEname());
		dao.setIndustry(dto.getIndustry());
		dao.setMemo(dto.getMemo());
		dao.setPhone(dto.getPhone());
		dao.setUnumber(dto.getUnumber());
		dao.setWebsite(dto.getWebsite());
		dao.setAddress(dto.getAddress());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);
		dao.setDetailUdate(now);
		dao.setDetailUuser(user);

		customerRepo.save(dao);

		return null;
	}

	@Transactional
	public ContactDto qryContact(Integer rowid) {
		Contact dao = contactRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return null;
		}
		ContactDto dto = new ContactDto();
		dto.setRowid(dao.getRowid());
		dto.setDname(dao.getDname());
		dto.setCname(dao.getCname());
		dto.setEname(dao.getEname());
		dto.setEmail(dao.getEmail());
		dto.setMobilePhone(dao.getMobilePhone());
		dto.setOfficePhone(dao.getOfficePhone());
		dto.setPosition(dao.getPosition());
		dto.setMemo(dao.getMemo());
		dto.setUdate(dao.getUdate());
		dto.setUuser(dao.getUuser());
		dto.setCdate(dao.getCdate());
		dto.setCuser(dao.getCuser());

		return dto;
	}

	@Transactional
	public String editContact(ContactDto dto) {
		Contact dao = contactRepo.findById(dto.getRowid()).orElse(null);
		if (dao == null) {
			return "聯絡人不存在";
		}

		dao.setDname(dto.getDname());
		dao.setCname(dto.getCname());
		dao.setEname(dto.getEname());
		dao.setEmail(dto.getEmail());
		dao.setMobilePhone(dto.getMobilePhone());
		dao.setOfficePhone(dto.getOfficePhone());
		dao.setPosition(dto.getPosition());
		dao.setMemo(dto.getMemo());

		String user = userNameService.getCurrentUserName();

		dao.setUdate(new Date());
		dao.setUuser(user);

		contactRepo.save(dao);
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);
		return null;
	}

	@Transactional
	public List<ImportantRecordDto> qryImportantRecords(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getImportantRecords().stream().map(dao -> {
			ImportantRecordDto dto = new ImportantRecordDto();
			dto.setRowid(dao.getRowid());
			dto.setRecord(dao.getRecord());
			dto.setMarked(dao.getMarked());
			dto.setUdate(dao.getUdate());
			dto.setUuser(dao.getUuser());
			dto.setCdate(dao.getCdate());
			dto.setCuser(dao.getCuser());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createImportantRecord(Integer rowid, ImportantRecordDto dto) {

		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return "客戶資訊不存在";
		}

		ImportantRecord dao = new ImportantRecord();
		dao.setMarked(true);
		dao.setRecord(dto.getRecord());
		dao.setCustomer(customer);

		String user = userNameService.getCurrentUserName();
		Date now = new Date();

		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		importantRecordRepo.save(dao);
		customerRepo.updateDetailTime(rowid, user);
		return null;
	}

	@Transactional
	public String updateImportantRecord(Integer rowid, Boolean marked) {
		ImportantRecord dao = importantRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "重要事項不存在";
		}
		dao.setMarked(marked);

		String user = userNameService.getCurrentUserName();
		dao.setUdate(new Date());
		dao.setUuser(user);

		importantRecordRepo.save(dao);
		customerRepo.updateDetailTime(rowid, user);
		return null;
	}

	@Transactional
	public String delImportantRecord(Integer rowid) {
		ImportantRecord dao = importantRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "重要事項不存在";
		}
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), userNameService.getCurrentUserName());
		importantRecordRepo.delete(dao);
		return null;
	}

	@Transactional
	public List<ProductDto> qryProducts(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getProducts().stream().map(dao -> {
			ProductDto dto = new ProductDto();
			dto.setRowid(dao.getRowid());
			dto.setSubject(dao.getSubject());
			dto.setInfoColumns(dao.getInfoColumns());
			dto.setInfoValues(dao.getInfoValues());
			dto.setUdate(dao.getUdate());
			dto.setUuser(dao.getUuser());
			dto.setCdate(dao.getCdate());
			dto.setCuser(dao.getCuser());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createProduct(Integer rowid, ProductDto dto) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return "客戶資訊不存在";
		}
		Product dao = new Product();

		dao.setSubject(dto.getSubject());
		dao.setInfoColumns(dto.getInfoColumns());
		dao.setInfoValues(dto.getInfoValues());
		dao.setCustomer(customer);

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		productRepo.save(dao);

		customerRepo.updateDetailTime(rowid, user);

		return null;
	}

	@Transactional
	public String delProduct(Integer rowid) {
		Product dao = productRepo.findById(rowid).orElse(null);

		if (dao == null) {
			return "產品資訊不存在";
		}
		String user = userNameService.getCurrentUserName();
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);

		productRepo.delete(dao);

		return null;
	}

	@Transactional
	public ProductDto qryProduct(Integer rowid) {
		Product dao = productRepo.findById(rowid).orElse(null);

		if (dao == null) {
			return null;
		}

		ProductDto dto = new ProductDto();
		dto.setRowid(dao.getRowid());
		dto.setSubject(dao.getSubject());
		dto.setInfoColumns(dao.getInfoColumns());
		dto.setInfoValues(dao.getInfoValues());
		dto.setUdate(dao.getUdate());
		dto.setUuser(dao.getUuser());
		dto.setCdate(dao.getCdate());
		dto.setCuser(dao.getCuser());
		return dto;

	}

	@Transactional
	public String editProduct(ProductDto dto) {
		Product dao = productRepo.findById(dto.getRowid()).orElse(null);

		if (dao == null) {
			return "產品資訊不存在";
		}

		dao.setSubject(dto.getSubject());
		dao.setInfoColumns(dto.getInfoColumns());
		dao.setInfoValues(dto.getInfoValues());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		productRepo.save(dao);

		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);

		return null;
	}

}
