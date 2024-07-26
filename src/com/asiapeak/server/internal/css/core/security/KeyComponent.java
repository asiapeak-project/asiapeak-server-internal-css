package com.asiapeak.server.internal.css.core.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyComponent {

	@Bean
	@Qualifier("jwtKeyPair")
	KeyPair jwtKeyPair() throws NoSuchAlgorithmException {
		return generateRSAKeyPair(2048);
	}

	@Bean
	@Qualifier("jwtPrivateKey")
	RSAPrivateKey jwtPrivateKey() throws NoSuchAlgorithmException {
		return (RSAPrivateKey) jwtKeyPair().getPrivate();
	}

	@Bean
	@Qualifier("jwtPublicKey")
	RSAPublicKey jwtPublicKey() throws NoSuchAlgorithmException {
		return (RSAPublicKey) jwtKeyPair().getPublic();
	}

	private static KeyPair generateRSAKeyPair(int size) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(size, new SecureRandom());
		return generator.generateKeyPair();
	}

}
