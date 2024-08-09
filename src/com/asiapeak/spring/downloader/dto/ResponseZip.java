package com.asiapeak.spring.downloader.dto;

import java.io.File;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.asiapeak.spring.downloader.function.ZipFilterPredicate;

public class ResponseZip {

	String zipName;

	File folder;

	List<File> files;

	ZipFilterPredicate filter;

	int level = ZipOutputStream.DEFLATED;

	protected ResponseZip() {

	}

	public ResponseZip(File folder) {
		this.folder = folder;
	}

	public ResponseZip(List<File> files) {
		this.files = files;
	}

	public String getZipName() {
		return zipName;
	}

	public void setZipName(String zipName) {
		this.zipName = zipName;
	}

	public ZipFilterPredicate getFilter() {
		return filter;
	}

	public void setFilter(ZipFilterPredicate filter) {
		this.filter = filter;
	}

	public File getFolder() {
		return folder;
	}

	public List<File> getFiles() {
		return files;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * Sets the compression level for subsequent entries which are DEFLATED. The
	 * default setting is DEFAULT_COMPRESSION.
	 * 
	 * @param level the compression level (0-9)
	 * @exception IllegalArgumentException if the compression level is invalid
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
