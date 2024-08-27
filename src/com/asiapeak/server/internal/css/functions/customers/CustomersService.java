package com.asiapeak.server.internal.css.functions.customers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.asiapeak.server.internal.css.dao.entity.Contact;
import com.asiapeak.server.internal.css.dao.entity.Customer;
import com.asiapeak.server.internal.css.dao.entity.Deployment;
import com.asiapeak.server.internal.css.dao.entity.Document;
import com.asiapeak.server.internal.css.dao.entity.Product;
import com.asiapeak.server.internal.css.dao.entity.ServiceRecord;
import com.asiapeak.server.internal.css.dao.entity.ServiceRecordHandle;
import com.asiapeak.server.internal.css.dao.repo.ContactRepo;
import com.asiapeak.server.internal.css.dao.repo.CustomerRepo;
import com.asiapeak.server.internal.css.dao.repo.DeploymentRepo;
import com.asiapeak.server.internal.css.dao.repo.DocumentRepo;
import com.asiapeak.server.internal.css.dao.repo.ProductRepo;
import com.asiapeak.server.internal.css.dao.repo.ServiceRecordHandleRepo;
import com.asiapeak.server.internal.css.dao.repo.ServiceRecordRepo;
import com.asiapeak.server.internal.css.functions.FileService;
import com.asiapeak.server.internal.css.functions.customers.dto.ContactDto;
import com.asiapeak.server.internal.css.functions.customers.dto.CustomerDto;
import com.asiapeak.server.internal.css.functions.customers.dto.DeploymentDto;
import com.asiapeak.server.internal.css.functions.customers.dto.DeploymentOutptuDto;
import com.asiapeak.server.internal.css.functions.customers.dto.DocumentInputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.DocumentOutputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ProductDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ServiceRecordHandleOutputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ServiceRecordInputDto;
import com.asiapeak.server.internal.css.functions.customers.dto.ServiceRecordOutputDto;
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
	ProductRepo productRepo;

	@Autowired
	DeploymentRepo deploymentRepo;

	@Autowired
	DocumentRepo documentRepo;

	@Autowired
	FileService fileService;

	@Autowired
	ServiceRecordRepo serviceRecordRepo;

	@Autowired
	ServiceRecordHandleRepo serviceRecordHandleRepo;

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

	@Transactional
	public List<DeploymentDto> qryDeployments(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getDeployments().stream().map(dao -> {
			DeploymentDto dto = new DeploymentDto();
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
	public String createDeployment(Integer rowid, DeploymentDto dto) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return "客戶資訊不存在";
		}
		Deployment dao = new Deployment();

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

		deploymentRepo.save(dao);

		customerRepo.updateDetailTime(rowid, user);

		return null;
	}

	@Transactional
	public String delDeployment(Integer rowid) {
		Deployment dao = deploymentRepo.findById(rowid).orElse(null);

		if (dao == null) {
			return "佈署環境不存在";
		}
		String user = userNameService.getCurrentUserName();
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);

		deploymentRepo.delete(dao);

		return null;
	}

	@Transactional
	public DeploymentDto qryDeployment(Integer rowid) {
		Deployment dao = deploymentRepo.findById(rowid).orElse(null);

		if (dao == null) {
			return null;
		}

		DeploymentDto dto = new DeploymentDto();
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
	public String editDeployment(ProductDto dto) {
		Deployment dao = deploymentRepo.findById(dto.getRowid()).orElse(null);

		if (dao == null) {
			return "佈署環境不存在";
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

		deploymentRepo.save(dao);

		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);

		return null;
	}

	@Transactional
	public List<String> qryDocumentCatetories() {
		return documentRepo.findDistinctCategories();
	}

	@Transactional
	public List<DeploymentOutptuDto> qryDocuments(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getDocuments().stream().map(dao -> {
			DeploymentOutptuDto dto = new DeploymentOutptuDto();
			dto.setRowid(dao.getRowid());
			dto.setCategory(dao.getCategory());
			dto.setSubject(dao.getSubject());
			dto.setUdate(dao.getUdate());
			dto.setUuser(dao.getUuser());
			dto.setCdate(dao.getCdate());
			dto.setCuser(dao.getCuser());
			dto.setAttachments(fileService.listDocumentFiles(rowid, dao.getRowid()).size());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public void createDocument(Integer rowid, DocumentInputDto dto) throws Exception {

		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			throw new RuntimeException("客戶資訊不存在");
		}

		Document dao = new Document();
		dao.setCategory(dto.getCategory());
		dao.setSubject(dto.getSubject());
		dao.setContent(dto.getContent());
		dao.setCustomer(customer);

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		dao = documentRepo.save(dao);

		if (dto.getFiles() != null) {
			File documentFilder = fileService.getDocumentFolder(rowid, dao.getRowid());

			for (MultipartFile file : dto.getFiles()) {
				String fileName = file.getOriginalFilename();
				File saveFile = new File(documentFilder, fileName);
				file.transferTo(saveFile);
			}
		}

		customerRepo.updateDetailTime(customer.getRowid(), user);

	}

	@Transactional
	public DocumentOutputDto qryDocument(Integer rowid) {

		Document dao = documentRepo.findById(rowid).orElse(null);

		if (dao == null) {
			throw new RuntimeException("文件文檔不存在");
		}

		DocumentOutputDto dto = new DocumentOutputDto();
		dto.setParentRowid(dao.getCustomer().getRowid());
		dto.setRowid(dao.getRowid());
		dto.setCategory(dao.getCategory());
		dto.setSubject(dao.getSubject());
		dto.setContent(dao.getContent());
		dto.setUdate(dao.getUdate());
		dto.setUuser(dao.getUuser());
		dto.setCdate(dao.getCdate());
		dto.setCuser(dao.getCuser());

		List<File> files = fileService.listDocumentFiles(dao.getCustomer().getRowid(), rowid);

		dto.setFiles(fileService.toAttachementDtos(files));

		return dto;
	}

	public File downloadAttachement(Integer parentRowid, Integer rowid, String filename) throws IOException {

		Customer customer = customerRepo.findById(parentRowid).orElse(null);
		if (customer == null) {
			throw new RuntimeException("客戶資訊不存在");
		}

		Document dao = documentRepo.findById(rowid).orElse(null);

		if (dao == null) {
			throw new RuntimeException("文件文檔不存在");
		}

		if (!dao.getCustomer().getRowid().equals(parentRowid)) {
			throw new RuntimeException("客戶資訊與文件文檔不符合");
		}

		File folder = fileService.getDocumentFolder(parentRowid, rowid);
		return new File(folder, filename);
	}

	public List<File> downloadAttachements(Integer parentRowid, Integer rowid) throws IOException {

		Customer customer = customerRepo.findById(parentRowid).orElse(null);
		if (customer == null) {
			throw new RuntimeException("客戶資訊不存在");
		}

		Document dao = documentRepo.findById(rowid).orElse(null);

		if (dao == null) {
			throw new RuntimeException("文件文檔不存在");
		}

		if (!dao.getCustomer().getRowid().equals(parentRowid)) {
			throw new RuntimeException("客戶資訊與文件文檔不符合");
		}

		File folder = fileService.getDocumentFolder(parentRowid, rowid);

		List<File> files = new ArrayList<>();

		for (File file : folder.listFiles()) {
			if (!file.isDirectory() && file.isFile()) {
				files.add(file);
			}
		}

		return files;
	}

	public String downloadAttachementsZipName(Integer parentRowid, Integer rowid) {

		Customer customer = customerRepo.findById(parentRowid).orElse(null);
		if (customer == null) {
			throw new RuntimeException("客戶資訊不存在");
		}

		Document dao = documentRepo.findById(rowid).orElse(null);

		if (dao == null) {
			throw new RuntimeException("文件文檔不存在");
		}

		if (!dao.getCustomer().getRowid().equals(parentRowid)) {
			throw new RuntimeException("客戶資訊與文件文檔不符合");
		}

		return String.format("%s_%s", customer.getDname(), dao.getSubject());
	}

	@Transactional
	public String qryDocumentSubject(Integer rowid) {
		Document dao = documentRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "文件文檔不存在";
		}
		return dao.getSubject();
	}

	@Transactional
	public void editDocument(Integer rowid, DocumentInputDto dto) throws IOException {
		Document dao = documentRepo.findById(rowid).orElse(null);
		if (dao == null) {
			throw new RuntimeException("文件文檔不存在");
		}
		dao.setCategory(dto.getCategory());
		dao.setSubject(dto.getSubject());
		dao.setContent(dto.getContent());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);

		File documentFilder = fileService.getDocumentFolder(dao.getCustomer().getRowid(), rowid);

		if (dto.getDelFiles() != null) {
			for (String delFile : dto.getDelFiles()) {
				File f = new File(documentFilder, delFile);
				if (f.exists()) {
					f.delete();
				}
			}
		}

		if (dto.getFiles() != null) {
			for (MultipartFile file : dto.getFiles()) {
				String fileName = file.getOriginalFilename();
				File saveFile = new File(documentFilder, fileName);
				file.transferTo(saveFile);
			}
		}

		documentRepo.save(dao);
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);
	}

	@Transactional
	public String delDocument(Integer rowid) throws IOException {
		Document dao = documentRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "文件文檔不存在";
		}

		File documentFilder = fileService.getDocumentFolder(dao.getCustomer().getRowid(), rowid);
		FileUtils.deleteQuietly(documentFilder);

		String user = userNameService.getCurrentUserName();
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);
		documentRepo.delete(dao);

		return null;
	}

	@Transactional
	public List<ServiceRecordOutputDto> qryServiceRecords(Integer rowid) {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			return new ArrayList<>();
		}

		return customer.getServiceRecords().stream().map(dao -> {
			ServiceRecordOutputDto dto = new ServiceRecordOutputDto();

			dto.setRowid(dao.getRowid());
			dto.setSubject(dao.getSubject());
			dto.setType(dao.getType());
			dto.setContactPerson(dao.getContactPerson());
			// dto.setServiceContent(dao.getServiceContent());
			dto.setHandleResult(dao.getHandleResult());
			dto.setServiceDate(dao.getServiceDate());

			dto.setUdate(dao.getUdate());
			dto.setUuser(dao.getUuser());
			dto.setCdate(dao.getCdate());
			dto.setCuser(dao.getCuser());

			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public List<String> qryServiceRecordTypes() {
		return serviceRecordRepo.findDistinctServiceTypes();
	}

	@Transactional
	public String createServiceRecord(Integer rowid, ServiceRecordOutputDto dto) {

		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			throw new RuntimeException("客戶資訊不存在");
		}

		ServiceRecord dao = new ServiceRecord();
		dao.setSubject(dto.getSubject());
		dao.setType(dto.getType());
		dao.setContactPerson(dto.getContactPerson());
		dao.setServiceContent(dto.getServiceContent());
		dao.setHandleResult(dto.getHandleResult());
		dao.setServiceDate(dto.getServiceDate());

		dao.setCustomer(customer);

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		serviceRecordRepo.save(dao);

		customerRepo.updateDetailTime(rowid, user);

		return null;
	}

	@Transactional
	public String createServiceRecord(Integer rowid, ServiceRecordInputDto dto) throws IOException {
		Customer customer = customerRepo.findById(rowid).orElse(null);
		if (customer == null) {
			throw new RuntimeException("客戶資訊不存在");
		}
		ServiceRecord dao = new ServiceRecord();
		dao.setSubject(dto.getSubject());
		dao.setType(dto.getType());
		dao.setContactPerson(dto.getContactPerson());
		dao.setServiceContent(dto.getServiceContent());
		dao.setHandleResult(dto.getHandleResult());
		dao.setServiceDate(dto.getServiceDate());
		dao.setCustomer(customer);

		dao = serviceRecordRepo.save(dao);

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);

		if (dto.getFiles() != null) {
			File folder = fileService.getServiceRecordFolder(rowid, dao.getRowid());

			for (MultipartFile file : dto.getFiles()) {
				String fileName = file.getOriginalFilename();
				File saveFile = new File(folder, fileName);
				file.transferTo(saveFile);
			}
		}

		customerRepo.updateDetailTime(customer.getRowid(), user);

		return null;
	}

	@Transactional
	public ServiceRecordOutputDto qryServiceRecord(Integer rowid) {
		ServiceRecord dao = serviceRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return null;
		}

		ServiceRecordOutputDto dto = new ServiceRecordOutputDto();
		dto.setRowid(dao.getRowid());
		dto.setCustomerRowid(dao.getCustomer().getRowid());
		dto.setSubject(dao.getSubject());
		dto.setType(dao.getType());
		dto.setContactPerson(dao.getContactPerson());
		dto.setServiceContent(dao.getServiceContent());
		dto.setHandleResult(dao.getHandleResult());
		dto.setServiceDate(dao.getServiceDate());
		dto.setCuser(dao.getCuser());
		dto.setCdate(dao.getCdate());
		dto.setUuser(dao.getUuser());
		dto.setUdate(dao.getUdate());

		List<File> files = fileService.listServiceRecordFiles(dao.getCustomer().getRowid(), rowid);

		dto.setFiles(fileService.toAttachementDtos(files));

		return dto;
	}

	@Transactional
	public List<ServiceRecordHandleOutputDto> qryServiceRecordHandles(Integer rowid) {

		ServiceRecord serviceRecord = serviceRecordRepo.findById(rowid).orElse(null);
		if (serviceRecord == null) {
			return new ArrayList<>();
		}

		return serviceRecord.getServiceRecordHandles().stream().map(dao -> {
			ServiceRecordHandleOutputDto dto = new ServiceRecordHandleOutputDto();
			dto.setRowid(dao.getRowid());
			dto.setHandlePerson(dao.getHandlePerson());
			dto.setHandleContent(dao.getHandleContent());
			dto.setHandleDate(dao.getHandleDate());
			dto.setCuser(dao.getCuser());
			dto.setCdate(dao.getCdate());
			dto.setUuser(dao.getUuser());
			dto.setUdate(dao.getUdate());

			List<File> files = fileService.listServiceRecordHandleFiles(serviceRecord.getCustomer().getRowid(), dao.getRowid());
			dto.setFiles(fileService.toAttachementDtos(files));

			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public ServiceRecordHandleOutputDto qryServiceRecordHandle(Integer rowid) {

		ServiceRecordHandle dao = serviceRecordHandleRepo.findById(rowid).orElseThrow(() -> {
			return new RuntimeException("服務歷程不存在");
		});

		ServiceRecordHandleOutputDto dto = new ServiceRecordHandleOutputDto();
		dto.setRowid(dao.getRowid());
		dto.setHandlePerson(dao.getHandlePerson());
		dto.setHandleContent(dao.getHandleContent());
		dto.setHandleDate(dao.getHandleDate());
		dto.setCuser(dao.getCuser());
		dto.setCdate(dao.getCdate());
		dto.setUuser(dao.getUuser());
		dto.setUdate(dao.getUdate());

		return dto;
	}

	@Transactional
	public String editServiceRecord(Integer rowid, ServiceRecordInputDto dto) throws IOException {
		ServiceRecord dao = serviceRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			throw new RuntimeException("服務歷程不存在");
		}

		dao.setSubject(dto.getSubject());
		dao.setType(dto.getType());
		dao.setContactPerson(dto.getContactPerson());
		dao.setServiceContent(dto.getServiceContent());
		dao.setHandleResult(dto.getHandleResult());
		dao.setServiceDate(dto.getServiceDate());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();
		dao.setUdate(now);
		dao.setUuser(user);

		File folder = fileService.getServiceRecordFolder(dao.getCustomer().getRowid(), rowid);

		if (dto.getDelFiles() != null) {
			for (String delFile : dto.getDelFiles()) {
				File f = new File(folder, delFile);
				if (f.exists()) {
					f.delete();
				}
			}
		}

		if (dto.getFiles() != null) {
			for (MultipartFile file : dto.getFiles()) {
				String fileName = file.getOriginalFilename();
				File saveFile = new File(folder, fileName);
				file.transferTo(saveFile);
			}
		}

		serviceRecordRepo.save(dao);

		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);

		return null;
	}

	@Transactional
	public String delServiceRecord(Integer rowid) {
		ServiceRecord dao = serviceRecordRepo.findById(rowid).orElse(null);
		if (dao == null) {
			throw new RuntimeException("服務歷程不存在");
		}
		String user = userNameService.getCurrentUserName();
		customerRepo.updateDetailTime(dao.getCustomer().getRowid(), user);
		serviceRecordRepo.delete(dao);

		return null;
	}

}
