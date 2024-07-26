package com.asiapeak.server.internal.css.core.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JWTUtils {

	public static String encode(JWTClaimsSet claimsSet, RSAPrivateKey privateKey, RSAPublicKey publicKey) throws Exception {
		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);

		JWSSigner signer = new RSASSASigner(privateKey);
		signedJWT.sign(signer);

		JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_512, EncryptionMethod.A128GCM).contentType("JWT").build(), new Payload(signedJWT));

		JWEEncrypter encrypter = new RSAEncrypter(publicKey);
		jweObject.encrypt(encrypter);

		return jweObject.serialize();
	}

	public static JWTClaimsSet decode(String jwt, RSAPrivateKey privateKey, RSAPublicKey publicKey) throws Exception {
		JWEObject jweObject = JWEObject.parse(jwt);
		JWEDecrypter decrypter = new RSADecrypter(privateKey);
		jweObject.decrypt(decrypter);

		SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

		JWSVerifier verifier = new RSASSAVerifier(publicKey);
		if (!signedJWT.verify(verifier)) {
			throw new RuntimeException("JWT signature is valid");
		}
		return signedJWT.getJWTClaimsSet();
	}
}
