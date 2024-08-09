package com.asiapeak.server.internal.css.functions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileService {

	@Value("${com.asiapeak.server.internal.css.data-folder}")
	String dataFolder;

	public List<File> listDocumentFiles(Integer customerRowid, Integer documentRowid) {

		File dataFolderFile = new File(dataFolder);

		if (!dataFolderFile.exists() || !dataFolderFile.isDirectory()) {
			return new ArrayList<>();
		}

		File customerFolder = new File(dataFolderFile, customerRowid.toString());
		if (!customerFolder.exists()) {
			return new ArrayList<>();
		}

		File documentFilder = new File(customerFolder, documentRowid.toString());
		if (!documentFilder.exists()) {
			return new ArrayList<>();
		}

		return Arrays.asList(documentFilder.listFiles());
	}

	public File getDocumentFolder(Integer customerRowid, Integer documentRowid) throws IOException {
		File dataFolderFile = new File(dataFolder);

		if (!dataFolderFile.exists() || !dataFolderFile.isDirectory()) {
			throw new IOException("Configer " + dataFolder + "is not a directory.");
		}

		File customerFolder = new File(dataFolderFile, customerRowid.toString());
		if (!customerFolder.exists()) {
			customerFolder.mkdir();
		}

		File documentFilder = new File(customerFolder, documentRowid.toString());
		if (!documentFilder.exists()) {
			documentFilder.mkdir();
		}

		return documentFilder;
	}

}
