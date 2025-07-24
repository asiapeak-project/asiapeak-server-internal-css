package com.asiapeak.server.internal.css.functions.customers.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AttachementDto {
	String name;
	String urlEncoded;
	long size;
	Date udate;
}
