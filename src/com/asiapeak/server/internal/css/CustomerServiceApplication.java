package com.asiapeak.server.internal.css;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.asiapeak.server.internal.css.core.user.ClearThreadAuthRoleFilter;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;

@EnableScheduling
@Configuration
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, ThymeleafAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
@ComponentScan("com.asiapeak")
public class CustomerServiceApplication implements WebMvcConfigurer {

	public static final String RESOURCE_UUID = UUID.randomUUID().toString();

	public static void main(String[] args) {
		System.setProperty("application.work.dir", System.getProperty("user.dir"));
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/resources/");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/favicon.ico");
	}

	@Bean
	SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("/static/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);
		templateEngine.addDialect(new LayoutDialect(new GroupingStrategy()));
		templateEngine.setTemplateResolver(templateResolver);
		return templateEngine;
	}

	@Bean
	ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	FilterRegistrationBean<ClearThreadAuthRoleFilter> clearThreadAuthRoleFilter() {
		FilterRegistrationBean<ClearThreadAuthRoleFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ClearThreadAuthRoleFilter());
		registrationBean.setOrder(Integer.MAX_VALUE);
		return registrationBean;
	}

}
