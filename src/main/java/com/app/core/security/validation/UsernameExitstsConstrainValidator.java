package com.app.core.security.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.app.core.security.annotation.UsernameExistsConstraint;
import com.app.core.security.service.AuthService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.RequiredArgsConstructor;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@Component
@RequiredArgsConstructor
public class UsernameExitstsConstrainValidator implements ConstraintValidator<UsernameExistsConstraint, String>{
	
	private Log log = LogFactory.getLog(UsernameExitstsConstrainValidator.class);
	
	private final AuthService authService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			return authService.isUsernameValid(value);	
		}catch (Exception e) {
				log.error(e);
		}
		return false;
	}

}
