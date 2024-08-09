package com.asiapeak.spring.downloader.abstracts;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public abstract class AbstractDownloadHandler<T> extends AbstractHttpMessageConverter<T> {

	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 16;
	protected static final long DEFAULT_EXPIRE_TIME = 604800000L;
	protected static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	public abstract Class<?> supportType();

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return supportType() == clazz;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return supportType() == clazz;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new HttpMessageNotReadableException("Type Not Readable", inputMessage);
	}

	@Override
	protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		throw new HttpMessageNotWritableException("Invoked wrong method.");
	}

	protected abstract void write(T t, HttpServletRequest request, HttpServletResponse response) throws IOException;

	@SuppressWarnings("unchecked")
	public void doWrite(Object obj, HttpServletRequest request, HttpServletResponse response) throws HttpMessageNotWritableException, IOException {
		response.reset();
		try {
			write((T) obj, request, response);
		} catch (ClientAbortException e) {
			// no need to handle
		} catch (IOException e) {
			throw e;
		}

	}

	protected void copy(RandomAccessFile input, OutputStream output, long start, long length) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int read;

		if (input.length() == length) {
			while ((read = input.read(buffer)) > 0) {
				output.write(buffer, 0, read);
			}
		} else {
			input.seek(start);
			long toRead = length;

			while ((read = input.read(buffer)) > 0) {
				if ((toRead -= read) > 0) {
					output.write(buffer, 0, read);
				} else {
					output.write(buffer, 0, (int) toRead + read);
					break;
				}
			}
		}
	}

	protected void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException ignore) {

			}
		}
	}

	protected String encodeFileName(String fileName) throws IOException {
		return URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
	}

	protected boolean matches(String matchHeader, String toMatch) {
		String[] matchValues = matchHeader.split("\\s*,\\s*");
		Arrays.sort(matchValues);
		return Arrays.binarySearch(matchValues, toMatch) > -1 || Arrays.binarySearch(matchValues, "*") > -1;
	}

	protected long sublong(String value, int beginIndex, int endIndex) {
		String substring = value.substring(beginIndex, endIndex);
		return substring.isEmpty() ? -1 : Long.parseLong(substring);
	}

	protected boolean accepts(String acceptHeader, String toAccept) {
		String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
		Arrays.sort(acceptValues);
		return Arrays.binarySearch(acceptValues, toAccept) > -1 || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1 || Arrays.binarySearch(acceptValues, "*/*") > -1;
	}

	protected boolean allowGzip(MediaType mediaType) {
		String mediaTypeString = mediaType.toString();
		return mediaTypeString.startsWith("text/") || //
				mediaTypeString.startsWith("image/") || //
				mediaTypeString.startsWith("application/"); //
	}
}
