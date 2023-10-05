package com.app.core.entity.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArgumentNotValidDto {
	private String code;
	private List<ErrorDto> errors;
}
