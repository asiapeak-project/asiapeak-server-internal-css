package com.asiapeak.server.internal.css.core.user;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class OneWayHash {

	public static String MD5(String str) {
		return doHash(str, "MD5");
	}

	public static String SHA256(String str) {
		return doHash(str, "SHA-256");
	}

	public static String SHA512(String str) {
		return doHash(str, "SHA-512");
	}

	private static String doHash(String str, String algorithm) {
		try {
			if (str == null || "".equals(str)) {
				return "";
			}
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return DatatypeConverter.printHexBinary(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
