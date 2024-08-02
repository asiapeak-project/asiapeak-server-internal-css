package com.asiapeak.server.internal.css.core.security.totp;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;

@Component
public class TotpUtils {

	@Autowired
	SecretGenerator secretGenerator;

	@Autowired
	QrGenerator qrGenerator;

	@Autowired
	CodeVerifier codeVerifier;

	/**
	 * Generate Secret Key
	 * 
	 * @return String
	 */
	public String generateSecret() {
		return secretGenerator.generate();
	}

	/**
	 * Generate QR Code Image Base64 format
	 * 
	 * @param label  example@example.com
	 * @param issuer YourApp
	 * @param secret secret key
	 * @return Base64 String
	 * 
	 * @throws QrGenerationException
	 */
	public String generateQrCode(String label, String issuer, String secret) throws QrGenerationException {
		QrData qrData = new QrData.Builder().label(label).secret(secret).issuer(issuer).build();
		byte[] qrbytes = qrGenerator.generate(qrData);
		return Base64.getEncoder().encodeToString(qrbytes);
	}

	/**
	 * Verify Code
	 * 
	 * @param secret
	 * @param code
	 * @return boolean
	 */
	public boolean verify(String secret, String code) {
		return codeVerifier.isValidCode(secret, code);
	}

}
