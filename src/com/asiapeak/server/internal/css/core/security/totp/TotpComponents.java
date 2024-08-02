package com.asiapeak.server.internal.css.core.security.totp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

@Configuration
public class TotpComponents {

	@Bean
	SecretGenerator secretGenerator() {
		return new DefaultSecretGenerator();
	}

	@Bean
	QrGenerator qrGenerator() {
		return new ZxingPngQrGenerator();
	}

	@Bean
	CodeVerifier codeVerifier() {
		CodeGenerator cg = new DefaultCodeGenerator();
		TimeProvider tp = new SystemTimeProvider();
		CodeVerifier cv = new DefaultCodeVerifier(cg, tp);
		return cv;
	}

}
