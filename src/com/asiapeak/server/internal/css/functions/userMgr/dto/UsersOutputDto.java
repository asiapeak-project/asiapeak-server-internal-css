package com.asiapeak.server.internal.css.functions.userMgr.dto;

import lombok.Data;

@Data
public class UsersOutputDto {
	Integer rowid;
	String account;
	Boolean hasTotp;
}
