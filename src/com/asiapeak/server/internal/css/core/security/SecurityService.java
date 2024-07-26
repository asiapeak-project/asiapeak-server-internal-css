package com.asiapeak.server.internal.css.core.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.JWTClaimsSet;

@Service
public class SecurityService {

	public final ThreadLocal<String> currentUser = new ThreadLocal<>();

	@Autowired
	@Qualifier("jwtPrivateKey")
	RSAPrivateKey jwtPrivateKey;

	@Autowired
	@Qualifier("jwtPublicKey")
	RSAPublicKey jwtPublicKey;

	/**
	 * create JWT
	 * 
	 * @param userName User's name
	 * @param request  HttpServletRequest
	 * @return JWT String
	 * @throws Exception
	 */
	public String createJWT(String userName, HttpServletRequest request) throws Exception {
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder() //
				.claim("name", userName) //
				.claim("ip", SecurityUtils.getClientIP(request)) //
				.expirationTime(DateUtils.addMinutes(new Date(), 30)) //
				.build();
		return JWTUtils.encode(claimsSet, jwtPrivateKey, jwtPublicKey);
	}

	/**
	 * verify JWT
	 * 
	 * @param jwt     JWT
	 * @param request HttpServletRequest
	 * @return User's name, null if not validated.
	 */
	public String verifyJWT(String jwt, HttpServletRequest request) {
		try {
			JWTClaimsSet claimsSet = JWTUtils.decode(jwt, jwtPrivateKey, jwtPublicKey);
			String ip = SecurityUtils.getClientIP(request);
			if (!ip.equals(claimsSet.getClaim("ip").toString())) {
				return null;
			}

			Date expirationTime = claimsSet.getExpirationTime();
			if (new Date().after(expirationTime)) {
				throw null;
			}

			return claimsSet.getClaim("name").toString();
		} catch (Exception e) {
			return null;
		}
	}

}
