package com.asiapeak.server.internal.css.dao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table
@Entity
@Accessors(chain = true)
public class ServiceRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Integer rowid;

	@Type(type = "text")
	String subject;

	/**
	 * 服務類型(諮詢/安裝/升級/除錯/客製化/其他)
	 */
	@Type(type = "text")
	String type;

	/**
	 * 聯絡對象
	 */
	@Type(type = "text")
	String contactPerson;

	/**
	 * 服務內容
	 */
	@Type(type = "text")
	String serviceContent;

	/**
	 * 處理結果(尚未處理/處理中/已解決)
	 */
	String handleResult;

	Date serviceDate;

	@Type(type = "text")
	String cuser;

	Date cdate;

	@Type(type = "text")
	String uuser;

	Date udate;

	@ManyToOne
	Customer customer;

	@OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL)
	List<ServiceRecordHandle> serviceRecordHandles;

}
