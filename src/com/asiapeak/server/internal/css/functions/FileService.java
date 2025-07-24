package com.asiapeak.server.internal.css.functions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.asiapeak.server.internal.css.functions.customers.dto.AttachementDto;

@Service
public class FileService {

	@Value("${com.asiapeak.server.internal.css.data-folder}")
	String dataFolder;

	public List<AttachementDto> toAttachementDtos(List<File> files) {
		return files.stream().map(file -> {
			AttachementDto attach = new AttachementDto();
			attach.setName(file.getName());
			attach.setUrlEncoded(encodFileName(file.getName()));
			attach.setSize(file.length());
			attach.setUdate(new Date(file.lastModified()));
			return attach;
		}).collect(Collectors.toList());
	}

	private String encodFileName(String fileName) {
		try {
			return URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private File getFolder(String name, Integer customerRowid, Integer targetRowid) throws IOException {
		File dataFolderFile = new File(dataFolder);

		if (!dataFolderFile.exists() || !dataFolderFile.isDirectory()) {
			throw new IOException("Configer " + dataFolder + "is not a directory.");
		}

		File customerFolder = new File(dataFolderFile, customerRowid.toString());
		if (!customerFolder.exists()) {
			customerFolder.mkdir();
		}

		File parentFolder = new File(customerFolder, name);
		if (!parentFolder.exists()) {
			parentFolder.mkdir();
		}

		File contentFolder = new File(parentFolder, targetRowid.toString());
		if (!contentFolder.exists()) {
			contentFolder.mkdir();
		}

		return contentFolder;
	}

	private List<File> listFiles(String name, Integer customerRowid, Integer targetRowid) {
		File dataFolderFile = new File(dataFolder);

		if (!dataFolderFile.exists() || !dataFolderFile.isDirectory()) {
			return new ArrayList<>();
		}

		File customerFolder = new File(dataFolderFile, customerRowid.toString());
		if (!customerFolder.exists()) {
			return new ArrayList<>();
		}

		File parentFolder = new File(customerFolder, name);
		if (!parentFolder.exists()) {
			return new ArrayList<>();
		}

		File documentFolder = new File(parentFolder, targetRowid.toString());
		if (!documentFolder.exists()) {
			return new ArrayList<>();
		}

		return Arrays.asList(documentFolder.listFiles()).stream().filter(f -> f.isFile()).collect(Collectors.toList());
	}

	public List<File> listDocumentFiles(Integer customerRowid, Integer documentRowid) {
		return listFiles("document", customerRowid, documentRowid);
	}

	public File getDocumentFolder(Integer customerRowid, Integer documentRowid) throws IOException {
		return getFolder("document", customerRowid, documentRowid);
	}

	public File getServiceRecordFolder(Integer customerRowid, Integer serviceRecordRowid) throws IOException {
		return getFolder("serviceRecord", customerRowid, serviceRecordRowid);
	}

	public List<File> listServiceRecordFiles(Integer customerRowid, Integer serviceRecordRowid) {
		return listFiles("serviceRecord", customerRowid, serviceRecordRowid);
	}

	public File getServiceRecordHandleFolder(Integer customerRowid, Integer serviceRecordHandleRowid) throws IOException {
		return getFolder("serviceRecordHandle", customerRowid, serviceRecordHandleRowid);
	}

	public List<File> listServiceRecordHandleFiles(Integer customerRowid, Integer serviceRecordHandleRowid) {
		return listFiles("serviceRecordHandle", customerRowid, serviceRecordHandleRowid);
	}

}
