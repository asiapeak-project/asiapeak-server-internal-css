package com.asiapeak.server.internal.css.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table
@Entity
@Accessors(chain = true)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Integer rowid;

	@Type(type = "text")
	String account;

	@Type(type = "text")
	String password;

	@Type(type = "text")
	String secret;
}
