package com.asiapeak.server.internal.css.core.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.asiapeak.server.internal.css.core.user.UserAuthService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig implements AuthenticationEntryPoint, AccessDeniedHandler {

	final static ThreadLocal<Throwable> errorException = new ThreadLocal<Throwable>();

	public static final List<String> securityUrlPermits = Collections.unmodifiableList(Arrays.asList(new String[] { //
			"/", //
			"/createAdmin",
			"/login", //
			"/logout", //
			"/resources/**",//
	}));

	@Autowired
	UserAuthService userAuthService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http //
				.cors(cors -> cors.disable()) //
				.csrf(csrf -> csrf.disable()) //
				.headers(header -> header.frameOptions().sameOrigin()) //
				.formLogin(login -> login.disable()) //
				.logout(logout -> logout.disable()) //
				.httpBasic(basic -> basic.disable()) //
				.anonymous(anonymous -> anonymous.disable()) //
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)) //
				.exceptionHandling(handling -> handling.authenticationEntryPoint(this).accessDeniedHandler(this)) //
				.authorizeHttpRequests(authorize -> {
					securityUrlPermits.forEach(url -> {
						authorize.antMatchers(url).permitAll();
					});
					authorize.anyRequest().authenticated();
				}) //
				.build();
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.sendRedirect("/accessDenied");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.sendRedirect("/");
	}

}
