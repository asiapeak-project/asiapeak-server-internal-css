package com.asiapeak.server.internal.css.system.dto;

import lombok.Data;

@Data
public class ChangePasswordInputDto {
	String oldPassword;
	String newPassword;
}
