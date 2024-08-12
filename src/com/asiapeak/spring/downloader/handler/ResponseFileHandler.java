package com.asiapeak.spring.downloader.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;

import com.asiapeak.spring.downloader.abstracts.AbstractDownloadHandler;
import com.asiapeak.spring.downloader.dto.ResponseFile;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResponseFileHandler extends AbstractDownloadHandler<ResponseFile> {

	@Override
	public Class<?> supportType() {
		return ResponseFile.class;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return ResponseFile.class == clazz;
	}

	@Override
	protected void write(ResponseFile responseFile, HttpServletRequest request, HttpServletResponse response) throws IOException {

		File file = responseFile.getFile();

		Objects.requireNonNull(file, "file is null");

		if (!file.exists() || !file.isFile()) {
			throw new FileNotFoundException(file.getPath());
		}

		String fileName = responseFile.getFileName();

		if (fileName == null || "".equals(fileName)) {
			fileName = file.getName();
		}

		long length = file.length();
		long lastModified = file.lastModified();
		String eTag = encodeFileName(fileName) + "_" + length + "_" + lastModified;
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

		MediaRangeBean full = new MediaRangeBean(0, length - 1, length);
		List<MediaRangeBean> ranges = new ArrayList<MediaRangeBean>();

		String range = request.getHeader("Range");

		if (range != null) {
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes */" + length);
				response.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
				return;
			}

			String ifRange = request.getHeader("If-Range");
			if (ifRange != null && !ifRange.equals(eTag)) {
				try {
					long ifRangeTime = request.getDateHeader("If-Range"); // Throws IAE if invalid.
					if (ifRangeTime != -1 && ifRangeTime + 1000 < lastModified) {
						ranges.add(full);
					}
				} catch (IllegalArgumentException ignore) {
					ranges.add(full);
				}
			}

			if (ranges.isEmpty()) {
				for (String part : range.substring(6).split(",")) {
					long start = sublong(part, 0, part.indexOf("-"));
					long end = sublong(part, part.indexOf("-") + 1, part.length());
					if (start == -1) {
						start = length - end;
						end = length - 1;
					} else if (end == -1 || end > length - 1) {
						end = length - 1;
					}
					if (start > end) {
						response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes */" + length);
						response.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
						return;
					}
					ranges.add(new MediaRangeBean(start, end, length));
				}
			}

		}

		MediaType mediaType = MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM);

		String acceptEncoding = request.getHeader("Accept-Encoding");
		boolean acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip") && allowGzip(mediaType);
		String disposition = "inline";

		if (!mediaType.toString().startsWith("image")) {
			String accept = request.getHeader("Accept");
			disposition = accept != null && accepts(accept, mediaType.toString()) ? "inline" : "attachment";
		}

		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition + ";filename=\"" + encodeFileName(fileName) + "\"");
		response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
		response.setHeader(HttpHeaders.ETAG, eTag);
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModified);
		response.setDateHeader(HttpHeaders.EXPIRES, expires);

		try (RandomAccessFile input = new RandomAccessFile(file, "r")) {
			OutputStream output = response.getOutputStream();

			if (ranges.isEmpty() || ranges.get(0) == full) {
				MediaRangeBean r = full;
				response.setContentType(mediaType.toString());
				if (acceptsGzip) {
					mediaType = new MediaType(mediaType, StandardCharsets.UTF_8);
					response.setHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
					output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
				} else {
					response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(r.length));
				}
				response.setStatus(HttpStatus.OK.value());
				copy(input, output, r.start, r.length);

			} else if (ranges.size() == 1) {
				MediaRangeBean r = ranges.get(0);
				response.setContentType(mediaType.toString());
				response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + r.start + "-" + r.end + "/" + r.total);
				response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(r.length));
				response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
				copy(input, output, r.start, r.length);

			} else {
				mediaType = new MediaType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
				response.setContentType(mediaType.toString());
				response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
				ServletOutputStream sos = (ServletOutputStream) output;
				for (MediaRangeBean r : ranges) {
					sos.println();
					sos.println("--" + MULTIPART_BOUNDARY);
					sos.println("Content-Type: " + mediaType.toString());
					sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);
					copy(input, output, r.start, r.length);
				}
				sos.println();
				sos.println("--" + MULTIPART_BOUNDARY + "--");
			}
			close(output);
		}

	}

	private class MediaRangeBean {
		private long start;
		private long end;
		private long length;
		private long total;

		private MediaRangeBean(long start, long end, long total) {
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			this.total = total;
		}
	}

}
