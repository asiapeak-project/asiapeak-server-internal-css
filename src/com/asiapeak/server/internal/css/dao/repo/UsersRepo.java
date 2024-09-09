package com.asiapeak.server.internal.css.dao.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.DBConfig;
import com.asiapeak.server.internal.css.dao.entity.Users;
import com.asiapeak.server.internal.css.dao.enums.UserRole;

@Transactional(transactionManager = DBConfig.TransactionManager)
public interface UsersRepo extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {

	long countByRole(UserRole role);

	Optional<Users> findByAccountAndPassword(String account, String password);

}
