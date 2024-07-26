package com.asiapeak.server.internal.css.core.security;

import java.util.Arrays;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;

public class SecurityUtils {
	
	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	public static String getClientIP(ServerHttpRequest request) {

		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeaders().getFirst(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				if (ip.equals("0:0:0:0:0:0:0:1")) {
					ip = "127.0.0.1";
				}
				return ip;
			}
		}
		String ip = request.getRemoteAddress().getAddress().getHostAddress();
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	public static String getClientIP(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				if (ip.equals("0:0:0:0:0:0:0:1")) {
					ip = "127.0.0.1";
				}
				return ip;
			}
		}
		String ip = request.getRemoteAddr();
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	public static String getClientIP(ServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	public static String getURLWithContextPath(HttpServletRequest request) {
		return String.format("%s://%s%s", request.getScheme(), (request.getScheme().toLowerCase().endsWith("s") && request.getServerPort() == 443) || request.getServerPort() == 80 ? request.getServerName() : request.getServerName() + ":" + request.getServerPort(), request.getContextPath());
	}

	public static String getCookieValue(HttpServletRequest request, String key, String defaultValue) {
		if (request == null) {
			return defaultValue;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		return Arrays.stream(cookies).filter(c -> c.getName().equals(key)).findFirst().map(Cookie::getValue).orElse(defaultValue);
	}

	public static void setCookieValue(HttpServletRequest request, HttpServletResponse response, String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setHttpOnly(true);
		cookie.setSecure(request.getScheme().toLowerCase().equals("https"));
		cookie.setComment(key);
		if (StringUtils.isBlank(value)) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(Integer.MAX_VALUE);
		}
		response.addCookie(cookie);
	}
}
