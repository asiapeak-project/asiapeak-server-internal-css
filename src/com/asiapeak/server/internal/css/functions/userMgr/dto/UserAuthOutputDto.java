package com.asiapeak.server.internal.css.functions.userMgr.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserAuthOutputDto {
	String category;
	List<UserAuthItemOutputDto> auths;
}
