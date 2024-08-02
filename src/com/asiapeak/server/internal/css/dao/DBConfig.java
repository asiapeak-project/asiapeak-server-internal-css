package com.asiapeak.server.internal.css.dao;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories( //
		basePackages = "com.asiapeak.server.internal.css.dao.repo", //
		entityManagerFactoryRef = "entityManagerFactory", //
		transactionManagerRef = DBConfig.TransactionManager//
)
public class DBConfig {

	public static final String TransactionManager = "transactionManager";

	@Value("${hibernate.dialect}")
	public String hibernateDialect;

	@Value("${hibernate.show_sql:false}")
	public String hibernateShowSql;

	@Value("${hibernate.format_sql:false}")
	public String hibernateFormatQql;

	@Value("${hibernate.hbm2ddl.auto}")
	public String hibernateHbm2ddlAuto;

	@Value("${datasource.driverClassName}")
	public String datasourceDriverClassName;

	@Value("${datasource.jdbcUrl}")
	public String datasourceJdbcUrl;

	@Value("${datasource.username}")
	public String datasourceUsername;

	@Value("${datasource.password}")
	public String datasourcePassword;

	@Value("${datasource.maximumPoolSize:100}")
	public int datasourceMaximumPoolSize;

	@Value("${datasource.minimumIdle:0}")
	public int datasourceMinimumIdle;

	@Value("${datasource.idleTimeout:600000}")
	public long datasourceIdleTimeout;

	@Value("${datasource.connectionTestQuery}")
	public String datasourceConnectionTestQuery;

	@Value("${datasource.connectionTimeout:14400000}")
	public long datasourceConnectionTimeout;

	@Value("${datasource.maxLifetime:14400000}")
	public long datasourceMaxLifetime;

	@Bean
	@Qualifier("entityManagerFactory")
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan("com.asiapeak.server.internal.css.dao.entity");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.show_sql", hibernateShowSql);
		properties.setProperty("hibernate.format_sql", hibernateFormatQql);
		properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		em.setJpaProperties(properties);
		return em;
	}

	@Bean
	@Qualifier(DBConfig.TransactionManager)
	JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	@Qualifier("dataSource")
	DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(datasourceDriverClassName);
		config.setJdbcUrl(datasourceJdbcUrl);
		config.setUsername(datasourceUsername);
		config.setPassword(datasourcePassword);
		config.setMaximumPoolSize(datasourceMaximumPoolSize);
		config.setMinimumIdle(datasourceMinimumIdle);
		config.setIdleTimeout(datasourceIdleTimeout);
		config.setConnectionTestQuery(datasourceConnectionTestQuery);
		config.setConnectionTimeout(datasourceConnectionTimeout);
		config.setMaxLifetime(datasourceMaxLifetime);
		return new HikariDataSource(config);
	}

}
