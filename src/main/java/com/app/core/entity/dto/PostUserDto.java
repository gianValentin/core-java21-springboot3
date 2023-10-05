package com.app.core.entity.dto;

import com.app.core.utils.Constant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostUserDto{
		@NotBlank(message = "{name.notblank}")
		@Size(min = 2, max=32, message="{name.size}")
		String firstname;
		@NotBlank(message = "{username.notblank}")
		@Size(min = 2, max=32, message="{username.size")
		String username;
		@NotBlank(message = "{password.notblank}") 
		@Size(min = 2, max=20, message="{password.size}")
		String password;
		@NotBlank(message = "{lastname.notblank}") 
		@Size(min = 2, max=32, message="{lastname.size")
		String lastname;
		@NotEmpty(message = "{email.notempty}")
		@Email(message = "{email.notformat}",  regexp = Constant.EMAIL_REGEXP)
		String email;		
}
