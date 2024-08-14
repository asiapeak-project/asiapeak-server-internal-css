package com.asiapeak.server.internal.css.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.DBConfig;
import com.asiapeak.server.internal.css.dao.entity.ServiceRecord;

@Transactional(transactionManager = DBConfig.TransactionManager)
public interface ServiceRecordRepo extends JpaRepository<ServiceRecord, Integer>, JpaSpecificationExecutor<ServiceRecord> {

	@Query("SELECT DISTINCT d.type FROM ServiceRecord d")
	List<String> findDistinctServiceTypes();
	
}
