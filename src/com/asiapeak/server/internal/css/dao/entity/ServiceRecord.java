package com.asiapeak.server.internal.css.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	 * 服務類型(安裝/升級/詢問/除錯/等等等)
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
	 * 處理結果(0.尚未處理/1.無法解決/2.已解決)
	 */
	Integer handleResult;

	/**
	 * 處理人員
	 */
	@Type(type = "text")
	String handlePerson;

	/**
	 * 處理內容
	 */
	@Type(type = "text")
	String handleContent;

	Date serviceDate;

	Date handleDate;

	@Type(type = "text")
	String cuser;

	Date cdate;

	@Type(type = "text")
	String uuser;

	Date udate;

	@ManyToOne
	Customer customer;

}
