package com.asiapeak.spring.downloader.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import com.asiapeak.spring.downloader.abstracts.AbstractDownloadHandler;
import com.asiapeak.spring.downloader.dto.ResponseBytes;

public class ResponseBytesHandler extends AbstractDownloadHandler<ResponseBytes> {

	@Override
	public Class<?> supportType() {
		return ResponseBytes.class;
	}

	@Override
	protected void write(ResponseBytes responseBytes, HttpServletRequest request, HttpServletResponse response) throws IOException {

		byte[] bytes = responseBytes.getBytes();

		Objects.requireNonNull(bytes, "bytes is null");

		String fileName = responseBytes.getFileName();

		MediaType mediaType = null;

		if (fileName != null && !"".equals(fileName)) {
			long lastModified = responseBytes.getLastModified();
			String eTag = encodeFileName(fileName) + "_" + bytes.length + "_" + lastModified;
			long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;

			String ifNoneMatch = request.getHeader("If-None-Match");
			if (ifNoneMatch != null && matches(ifNoneMatch, eTag)) {
				response.setHeader(HttpHeaders.ETAG, eTag);
				response.setDateHeader(HttpHeaders.EXPIRES, expires);
				response.setStatus(HttpStatus.NOT_MODIFIED.value());
				return;
			}

			long ifModifiedSince = request.getDateHeader("If-Modified-Since");
			if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
				response.setHeader(HttpHeaders.ETAG, eTag);
				response.setDateHeader(HttpHeaders.EXPIRES, expires);
				response.setStatus(HttpStatus.NOT_MODIFIED.value());
				return;
			}

			String ifMatch = request.getHeader("If-Match");
			if (ifMatch != null && !matches(ifMatch, eTag)) {
				response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
				return;
			}

			long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
			if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
				response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
				return;
			}
			mediaType = Optional.ofNullable(responseBytes.getMediaType()).orElse(MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM));

			response.setHeader(HttpHeaders.ETAG, eTag);
			response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModified);
			response.setDateHeader(HttpHeaders.EXPIRES, expires);
		} else {
			mediaType = Optional.ofNullable(responseBytes.getMediaType()).orElse(MediaType.APPLICATION_OCTET_STREAM);
		}

		String acceptEncoding = request.getHeader("Accept-Encoding");
		boolean acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip") && allowGzip(mediaType);
		String disposition = "inline";

		if (!mediaType.toString().startsWith("image")) {
			String accept = request.getHeader("Accept");
			disposition = accept != null && accepts(accept, mediaType.toString()) ? "inline" : "attachment";
		}

		if (fileName != null && !"".equals(fileName)) {
			disposition += ";filename=\"" + encodeFileName(fileName) + "\"";
		}

		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition);
		response.setHeader(HttpHeaders.ACCEPT_RANGES, "none");
		response.setContentType(mediaType.toString());

		OutputStream output = response.getOutputStream();

		if (acceptsGzip) {
			mediaType = new MediaType(mediaType, StandardCharsets.UTF_8);
			response.setHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
			output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
		} else {
			response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));
		}
		response.setStatus(HttpStatus.OK.value());

		int offset = 0;
		int length = 0;
		while (offset < bytes.length) {
			length = Math.min(DEFAULT_BUFFER_SIZE, bytes.length - offset);
			output.write(bytes, offset, length);
			offset += length;
		}

		close(output);
	}

}
