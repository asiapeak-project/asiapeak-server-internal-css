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
import com.asiapeak.server.internal.css.dao.repo.ContactRepo;
import com.asiapeak.server.internal.css.dao.repo.CustomerRepo;
import com.asiapeak.server.internal.css.dao.repo.ImportantRecordRepo;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerContactDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerImportantRecordInputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerImportantRecordOutputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerInfoDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerInputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerOutputDto;

@Service
public class CustomersService {

	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	ContactRepo contactRepo;

	@Autowired
	ImportantRecordRepo importantRecordRepo;

	@Transactional
	public List<CustomerOutputDto> qryCustomers() {
		return customerRepo.findAll().stream().map(c -> {
			CustomerOutputDto dto = new CustomerOutputDto();
			dto.setRowid(c.getRowid());
			dto.setDname(c.getDname());
			dto.setCname(c.getCname());
			dto.setEname(c.getEname());
			dto.setUdate(c.getUdate());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String newCustomer(CustomerInputDto dto) {

		if (customerRepo.findByDname(dto.getDname()).isPresent()) {
			return "顯示名稱存在，請使用另一個名稱。";
		}

		Customer customer = new Customer();
		customer.setDname(dto.getDname());
		customer.setCname(dto.getCname());
		customer.setEname(dto.getEname());
		customer.setIndustry(dto.getIndustry());
		customer.setMemo(dto.getMemo());
		customer.setPhone(dto.getPhone());
		customer.setUdate(new Date());
		customer.setUnumber(dto.getUnumber());
		customer.setWebsite(dto.getWebsite());
		customer.setAddress(dto.getAddress());

		customerRepo.saveAndFlush(customer);

		return null;
	}

	@Transactional
	public Customer findCustomer(Integer rowid) {
		return customerRepo.findById(rowid).orElse(null);
	}

	@Transactional
	public CustomerInfoDto qryInfo(Integer rowid) {

		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return null;
		}

		CustomerInfoDto dto = new CustomerInfoDto();
		dto.setDname(customer.getDname());
		dto.setCname(customer.getCname());
		dto.setEname(customer.getEname());
		dto.setIndustry(customer.getIndustry());
		dto.setMemo(customer.getMemo());
		dto.setPhone(customer.getPhone());
		dto.setUdate(customer.getUdate());
		dto.setUnumber(customer.getUnumber());
		dto.setWebsite(customer.getWebsite());
		dto.setAddress(customer.getAddress());

		return dto;
	}

	@Transactional
	public List<CustomerContactDto> qryContact(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getContacts().stream().map(c -> {
			CustomerContactDto cdto = new CustomerContactDto();
			cdto.setRowid(c.getRowid());
			cdto.setDname(c.getDname());
			cdto.setCname(c.getCname());
			cdto.setEname(c.getEname());
			cdto.setEmail(c.getEmail());
			cdto.setMobilePhone(c.getMobilePhone());
			cdto.setOfficePhone(c.getOfficePhone());
			cdto.setPosition(c.getPosition());
			cdto.setMemo(c.getMemo());
			cdto.setUdate(c.getUdate());
			return cdto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createContact(Integer rowid, CustomerContactDto dto) {

		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return "客戶不存在！";
		}

		Contact contact = new Contact();
		contact.setDname(dto.getDname());
		contact.setCname(dto.getCname());
		contact.setEname(dto.getEname());
		contact.setEmail(dto.getEmail());
		contact.setMobilePhone(dto.getMobilePhone());
		contact.setOfficePhone(dto.getOfficePhone());
		contact.setPosition(dto.getPosition());
		contact.setMemo(dto.getMemo());
		contact.setUdate(new Date());
		contact.setCustomer(customer);

		contactRepo.save(contact);
		customerRepo.touchUdate(rowid);

		return null;
	}

	@Transactional
	public String delContact(Integer rowid) {

		Contact contact = contactRepo.findById(rowid).orElse(null);
		if (contact == null) {
			return "聯絡人不存在";
		}

		customerRepo.touchUdate(contact.getCustomer().getRowid());
		contactRepo.delete(contact);

		return null;
	}

	@Transactional
	public String editInfo(Integer rowid, CustomerInfoDto dto) {
		Customer customer = customerRepo.findById(rowid).orElse(null);

		if (customer == null) {
			return "客戶資訊不存在";
		}

		customer.setCname(dto.getCname());
		customer.setEname(dto.getEname());
		customer.setIndustry(dto.getIndustry());
		customer.setMemo(dto.getMemo());
		customer.setPhone(dto.getPhone());
		customer.setUnumber(dto.getUnumber());
		customer.setWebsite(dto.getWebsite());
		customer.setAddress(dto.getAddress());
		customer.setUdate(new Date());

		return null;
	}

	@Transactional
	public CustomerContactDto qryContactData(Integer rowid) {
		Contact c = contactRepo.findById(rowid).orElse(null);
		if (c == null) {
			return null;
		}
		CustomerContactDto cdto = new CustomerContactDto();
		cdto.setRowid(c.getRowid());
		cdto.setDname(c.getDname());
		cdto.setCname(c.getCname());
		cdto.setEname(c.getEname());
		cdto.setEmail(c.getEmail());
		cdto.setMobilePhone(c.getMobilePhone());
		cdto.setOfficePhone(c.getOfficePhone());
		cdto.setPosition(c.getPosition());
		cdto.setMemo(c.getMemo());
		cdto.setUdate(c.getUdate());

		return cdto;
	}

	@Transactional
	public String editContactData(CustomerContactDto dto) {
		Contact contact = contactRepo.findById(dto.getRowid()).orElse(null);
		if (contact == null) {
			return "聯絡人不存在";
		}

		contact.setDname(dto.getDname());
		contact.setCname(dto.getCname());
		contact.setEname(dto.getEname());
		contact.setEmail(dto.getEmail());
		contact.setMobilePhone(dto.getMobilePhone());
		contact.setOfficePhone(dto.getOfficePhone());
		contact.setPosition(dto.getPosition());
		contact.setMemo(dto.getMemo());
		contact.setUdate(new Date());

		contactRepo.save(contact);
		customerRepo.touchUdate(contact.getCustomer().getRowid());
		return null;
	}

	@Transactional
	public List<CustomerImportantRecordOutputDto> qryImportantRecords(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getImportantRecords().stream().map(i -> {
			CustomerImportantRecordOutputDto dto = new CustomerImportantRecordOutputDto();
			dto.setRowid(i.getRowid());
			dto.setRecord(i.getRecord());
			dto.setUdate(i.getUdate());
			dto.setMarked(i.getMarked());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createImportantRecord(Integer rowid, CustomerImportantRecordInputDto dto) {

		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return "客戶資訊不存在";
		}

		ImportantRecord dao = new ImportantRecord();
		dao.setMarked(true);
		dao.setRecord(dto.getRecord());
		dao.setUdate(new Date());
		dao.setCustomer(customer);

		importantRecordRepo.save(dao);
		customerRepo.touchUdate(rowid);
		return null;
	}

	@Transactional
	public String updateImportantRecord(Integer rowid, Boolean marked) {
		ImportantRecord dao = importantRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "重要事項不存在";
		}
		dao.setMarked(marked);
		dao.setUdate(new Date());
		importantRecordRepo.save(dao);
		customerRepo.touchUdate(rowid);
		return null;
	}

	@Transactional
	public String delImportantRecord(Integer rowid) {
		ImportantRecord dao = importantRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "重要事項不存在";
		}
		customerRepo.touchUdate(dao.getCustomer().getRowid());
		importantRecordRepo.delete(dao);
		;
		return null;
	}

}
