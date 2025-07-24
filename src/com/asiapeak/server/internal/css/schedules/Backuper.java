package com.asiapeak.server.internal.css.schedules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.asiapeak.server.internal.css.core.FileTools;
import com.asiapeak.server.internal.css.dao.entity.Contact;
import com.asiapeak.server.internal.css.dao.entity.Customer;
import com.asiapeak.server.internal.css.dao.entity.Deployment;
import com.asiapeak.server.internal.css.dao.entity.Document;
import com.asiapeak.server.internal.css.dao.entity.Product;
import com.asiapeak.server.internal.css.dao.entity.ServiceRecord;
import com.asiapeak.server.internal.css.dao.entity.ServiceRecordHandle;
import com.asiapeak.server.internal.css.dao.entity.Templates;
import com.asiapeak.server.internal.css.dao.entity.Users;
import com.asiapeak.server.internal.css.dao.entity.UsersAuth;
import com.asiapeak.server.internal.css.dao.repo.ContactRepo;
import com.asiapeak.server.internal.css.dao.repo.CustomerRepo;
import com.asiapeak.server.internal.css.dao.repo.DeploymentRepo;
import com.asiapeak.server.internal.css.dao.repo.DocumentRepo;
import com.asiapeak.server.internal.css.dao.repo.ProductRepo;
import com.asiapeak.server.internal.css.dao.repo.ServiceRecordHandleRepo;
import com.asiapeak.server.internal.css.dao.repo.ServiceRecordRepo;
import com.asiapeak.server.internal.css.dao.repo.TemplatesRepo;
import com.asiapeak.server.internal.css.dao.repo.UsersAuthRepo;
import com.asiapeak.server.internal.css.dao.repo.UsersRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Backuper {

	@Value("${com.asiapeak.server.internal.css.backup-folder}")
	String backupFolder;

	@Value("${com.asiapeak.server.internal.css.data-folder}")
	String dataFolder;

	@Autowired
	ContactRepo contactRepo;

	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	DeploymentRepo deploymentRepo;

	@Autowired
	DocumentRepo documentRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	ServiceRecordHandleRepo serviceRecordHandleRepo;

	@Autowired
	ServiceRecordRepo serviceRecordRepo;

	@Autowired
	TemplatesRepo templatesRepo;

	@Autowired
	UsersAuthRepo usersAuthRepo;

	@Autowired
	UsersRepo usersRepo;

	private boolean hasExecutedOnStartup = false;

	@Scheduled(initialDelay = 10000, fixedRate = Long.MAX_VALUE)
	public void executeOnStartup() throws Exception {
		if (!hasExecutedOnStartup) {
			run();
		}
	}

	@Scheduled(cron = "0 30 23 * * ?")
	public void run() throws Exception {
		hasExecutedOnStartup = true;
		
		log.info("Start Backup.....");

		log.info("Parsing contacts");
		List<Map<String, Object>> contacts = contactRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing customers");
		List<Map<String, Object>> customers = customerRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing deployment");
		List<Map<String, Object>> deployment = deploymentRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing documents");
		List<Map<String, Object>> documents = documentRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing products");
		List<Map<String, Object>> products = productRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing serviceRecordHandles");
		List<Map<String, Object>> serviceRecordHandles = serviceRecordHandleRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing serviceRecords");
		List<Map<String, Object>> serviceRecords = serviceRecordRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing templates");
		List<Map<String, Object>> templates = templatesRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing usersAuths");
		List<Map<String, Object>> usersAuths = usersAuthRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parsing users");
		List<Map<String, Object>> users = usersRepo.findAll().stream().map(m -> parse(m)).collect(Collectors.toList());

		log.info("Parse completed");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String time = sdf.format(new Date());

		File backupTargetFolder = new File(backupFolder, time);
		backupTargetFolder.mkdir();

		log.info("Backup temp folder=" + backupTargetFolder.getAbsolutePath());

		File contactFile = new File(backupTargetFolder, "contact" + "." + time + ".json");
		File customerFile = new File(backupTargetFolder, "customer" + "." + time + ".json");
		File deploymentFile = new File(backupTargetFolder, "deployment" + "." + time + ".json");
		File documentFile = new File(backupTargetFolder, "document" + "." + time + ".json");
		File productFile = new File(backupTargetFolder, "product" + "." + time + ".json");
		File serviceRecordHandleFile = new File(backupTargetFolder, "serviceRecordHandle" + "." + time + ".json");
		File serviceRecordFile = new File(backupTargetFolder, "serviceRecord" + "." + time + ".json");
		File templatesFile = new File(backupTargetFolder, "templates" + "." + time + ".json");
		File usersAuthFile = new File(backupTargetFolder, "usersAuth" + "." + time + ".json");
		File usersFile = new File(backupTargetFolder, "users" + "." + time + ".json");

		saveTableJson(contacts, contactFile);
		saveTableJson(customers, customerFile);
		saveTableJson(deployment, deploymentFile);
		saveTableJson(documents, documentFile);
		saveTableJson(products, productFile);
		saveTableJson(serviceRecordHandles, serviceRecordHandleFile);
		saveTableJson(serviceRecords, serviceRecordFile);
		saveTableJson(templates, templatesFile);
		saveTableJson(usersAuths, usersAuthFile);
		saveTableJson(users, usersFile);

		File dataFolderZip = new File(backupTargetFolder, "data-folder." + time + ".zip");
		zipFolder(new File(dataFolder), dataFolderZip);

		File resultZip = new File(backupFolder, time + ".zip");

		zipFolder(backupTargetFolder, resultZip);

		log.info("Delete temp folder=" + backupTargetFolder.getAbsolutePath());
		FileTools.deleteDirectory(backupTargetFolder);
		log.info("Backup completed");
	}

	private void saveTableJson(List<Map<String, Object>> list, File outFile) throws JsonProcessingException {
		log.info("Save Table Json=" + outFile.getName());
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(list);
		FileTools.writeString(outFile, json, false);
	}

	private void zipFolder(File folder, File outputZip) {
		log.info("Zip folder src=" + folder.getAbsolutePath());
		log.info("Zip folder to=" + outputZip.getAbsolutePath());
		try (FileOutputStream fos = new FileOutputStream(outputZip); ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipFolderRecursive(folder, folder.getName(), zos);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create zip file: " + outputZip.getAbsolutePath(), e);
		}
	}

	private void zipFolderRecursive(File folder, String basePath, ZipOutputStream zos) throws IOException {
		File[] files = folder.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			String entryPath = basePath + "/" + file.getName();

			if (file.isDirectory()) {
				// 如果是目錄，先建立目錄條目（以 / 結尾）
				ZipEntry dirEntry = new ZipEntry(entryPath + "/");
				zos.putNextEntry(dirEntry);
				zos.closeEntry();

				// 遞歸處理子目錄
				zipFolderRecursive(file, entryPath, zos);
			} else {
				// 如果是檔案，讀取內容並加入到 zip
				ZipEntry fileEntry = new ZipEntry(entryPath);
				zos.putNextEntry(fileEntry);

				try (FileInputStream fis = new FileInputStream(file)) {
					byte[] buffer = new byte[8192];
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}
				}

				zos.closeEntry();
			}
		}
	}

	private Map<String, Object> parse(Contact dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("dname", dao.getDname());
		map.put("cname", dao.getCname());
		map.put("ename", dao.getEname());
		map.put("email", dao.getEmail());
		map.put("mobilePhone", dao.getMobilePhone());
		map.put("officePhone", dao.getOfficePhone());
		map.put("position", dao.getPosition());
		map.put("memo", dao.getMemo());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("customer", dao.getCustomer() != null ? dao.getCustomer().getRowid() : null);
		return map;
	}

	private Map<String, Object> parse(Customer dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("dname", dao.getDname());
		map.put("cname", dao.getCname());
		map.put("ename", dao.getEname());
		map.put("phone", dao.getPhone());
		map.put("address", dao.getAddress());
		map.put("website", dao.getWebsite());
		map.put("unumber", dao.getUnumber());
		map.put("industry", dao.getIndustry());
		map.put("memo", dao.getMemo());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		map.put("detailUuser", dao.getDetailUuser());
		map.put("detailUdate", dao.getDetailUdate());
		// @OneToMany - 略過
		return map;
	}

	private Map<String, Object> parse(Deployment dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("subject", dao.getSubject());
		map.put("infoColumns", dao.getInfoColumns());
		map.put("infoValues", dao.getInfoValues());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("customer", dao.getCustomer() != null ? dao.getCustomer().getRowid() : null);
		return map;
	}

	private Map<String, Object> parse(Document dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("category", dao.getCategory());
		map.put("subject", dao.getSubject());
		map.put("content", dao.getContent());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("customer", dao.getCustomer() != null ? dao.getCustomer().getRowid() : null);
		return map;
	}

	private Map<String, Object> parse(Product dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("subject", dao.getSubject());
		map.put("infoColumns", dao.getInfoColumns());
		map.put("infoValues", dao.getInfoValues());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("customer", dao.getCustomer() != null ? dao.getCustomer().getRowid() : null);
		return map;
	}

	private Map<String, Object> parse(ServiceRecord dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("subject", dao.getSubject());
		map.put("type", dao.getType());
		map.put("contactPerson", dao.getContactPerson());
		map.put("serviceContent", dao.getServiceContent());
		map.put("handleResult", dao.getHandleResult());
		map.put("serviceDate", dao.getServiceDate());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("customer", dao.getCustomer() != null ? dao.getCustomer().getRowid() : null);
		// @OneToMany - 略過
		return map;
	}

	private Map<String, Object> parse(ServiceRecordHandle dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("handlePerson", dao.getHandlePerson());
		map.put("handleContent", dao.getHandleContent());
		map.put("handleDate", dao.getHandleDate());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("serviceRecord", dao.getServiceRecord() != null ? dao.getServiceRecord().getRowid() : null);
		return map;
	}

	private Map<String, Object> parse(Templates dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("name", dao.getName());
		map.put("infoColumns", dao.getInfoColumns());
		map.put("cuser", dao.getCuser());
		map.put("cdate", dao.getCdate());
		map.put("uuser", dao.getUuser());
		map.put("udate", dao.getUdate());
		return map;
	}

	private Map<String, Object> parse(Users dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("account", dao.getAccount());
		map.put("password", dao.getPassword());
		map.put("role", dao.getRole());
		map.put("secret", dao.getSecret());
		// @OneToMany - 略過
		return map;
	}

	private Map<String, Object> parse(UsersAuth dao) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("rowid", dao.getRowid());
		map.put("authName", dao.getAuthName());
		// @ManyToOne - 放入關聯物件的 @Id
		map.put("users", dao.getUsers() != null ? dao.getUsers().getRowid() : null);
		return map;
	}

}
