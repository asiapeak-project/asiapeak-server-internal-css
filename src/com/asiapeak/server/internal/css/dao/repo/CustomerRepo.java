package com.asiapeak.server.internal.css.dao.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.DBConfig;
import com.asiapeak.server.internal.css.dao.entity.Customer;

@Transactional(transactionManager = DBConfig.TransactionManager)
public interface CustomerRepo extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

	public Optional<Customer> findByDname(String dname);

}
