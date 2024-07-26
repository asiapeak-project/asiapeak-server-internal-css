package com.asiapeak.server.internal.css.core.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.asiapeak.server.internal.css.core.security.exceptions.TokenNotFoundException;
import com.asiapeak.server.internal.css.core.security.exceptions.TokenNotValidException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends OncePerRequestFilter implements AuthenticationEntryPoint, AccessDeniedHandler {

	final static ThreadLocal<Throwable> errorException = new ThreadLocal<Throwable>();

	public static final List<String> securityUrlPermits = Collections.unmodifiableList(Arrays.asList(new String[] { //
			"/", //
			"/login", //
			"/logout", //
			"/resources/**",//
	}));

	@Autowired
	SecurityService securityService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http //
				.cors(cors -> cors.disable()) //
				.csrf(csrf -> csrf.disable()) //
				.formLogin(login -> login.disable()) //
				.logout(logout -> logout.disable()) //
				.httpBasic(basic -> basic.disable()) //
				.anonymous(anonymous -> anonymous.disable()) //
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //
				.exceptionHandling(handling -> handling.authenticationEntryPoint(this).accessDeniedHandler(this)) //
				.authorizeHttpRequests(authorize -> {
					securityUrlPermits.forEach(url -> {
						authorize.antMatchers(url).permitAll();
					});
					authorize.anyRequest().authenticated();
				}) //
				.addFilterBefore(this, UsernamePasswordAuthenticationFilter.class) //
				.build();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		securityService.currentUser.remove();
		errorException.remove();

		try {
			String authToken = SecurityUtils.getCookieValue(request, HttpHeaders.AUTHORIZATION, null);

			if (StringUtils.isEmpty(authToken)) {
				String queryPath = request.getServletPath();
				throw new TokenNotFoundException("Token Not Found, path=" + queryPath);
			}

			String userName = securityService.verifyJWT(authToken, request);

			if (StringUtils.isEmpty(userName)) {
				String queryPath = request.getServletPath();
				throw new TokenNotValidException("Token Not Valid, path=" + queryPath);
			}

			List<GrantedAuthority> roles = new ArrayList<>();

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, roles);

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			securityService.currentUser.set(userName);
		} catch (Exception e) {
			errorException.set(e);
		} finally {
			filterChain.doFilter(request, response);
		}

	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.sendRedirect("/");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.sendRedirect("/");
	}

}
