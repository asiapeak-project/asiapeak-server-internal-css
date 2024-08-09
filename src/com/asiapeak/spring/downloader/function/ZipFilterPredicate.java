package com.asiapeak.spring.downloader.function;

import java.io.File;
import java.io.IOException;

import org.springframework.http.converter.HttpMessageNotWritableException;

@FunctionalInterface
public interface ZipFilterPredicate {
	boolean test(File file) throws IOException, HttpMessageNotWritableException;
}
