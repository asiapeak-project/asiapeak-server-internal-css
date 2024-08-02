package com.asiapeak.server.internal.css.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.DBConfig;
import com.asiapeak.server.internal.css.dao.entity.ContactRecord;

@Transactional(transactionManager = DBConfig.TransactionManager)
public interface ContactRecordRepo extends JpaRepository<ContactRecord, Integer>, JpaSpecificationExecutor<ContactRecord> {

}
