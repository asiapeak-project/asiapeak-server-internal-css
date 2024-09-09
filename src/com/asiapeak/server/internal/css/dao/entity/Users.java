package com.asiapeak.server.internal.css.dao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.asiapeak.server.internal.css.dao.enums.UserRole;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table
@Entity
@Accessors(chain = true)
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Integer rowid;

	@Column(unique = true)
	@Type(type = "text")
	String account;

	@Type(type = "text")
	String password;

	UserRole role;

	@Type(type = "text")
	String secret;
	
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
	List<UsersAuth> usersAuths;
}
