package com.app.core.entity.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetUserDto{
		Long id;		
		String firstname;		
		String username;		
		String password;		
		String lastname;	
		String email;
		Date createAt;

}
