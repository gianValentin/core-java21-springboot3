package com.app.core.entity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAllUserDto {
	private List<GetUserDto> getUserDto;
	private Integer size;

}
