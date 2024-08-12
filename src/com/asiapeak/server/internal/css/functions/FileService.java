package com.asiapeak.server.internal.css.functions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

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

		File documentParentFolder = new File(customerFolder, "document");
		if (!documentParentFolder.exists()) {
			return new ArrayList<>();
		}

		File documentFolder = new File(documentParentFolder, documentRowid.toString());
		if (!documentFolder.exists()) {
			return new ArrayList<>();
		}

		return Arrays.asList(documentFolder.listFiles()).stream().filter(f -> f.isFile()).collect(Collectors.toList());
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

		File documentParentFolder = new File(customerFolder, "document");
		if (!documentParentFolder.exists()) {
			documentParentFolder.mkdir();
		}

		File documentFilder = new File(documentParentFolder, documentRowid.toString());
		if (!documentFilder.exists()) {
			documentFilder.mkdir();
		}

		return documentFilder;
	}

}
