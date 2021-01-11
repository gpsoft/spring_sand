package jp.dip.gpsoft.springsand.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import jp.dip.gpsoft.springsand.service.UserService;

public class UniqueLoginIdValidator implements ConstraintValidator<UniqueLoginId, Object> {

	@Autowired
	private UserService userService;

	@Override
	public void initialize(UniqueLoginId annotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if ( value == null ) return true;
		return userService.isAvailableLoginId((String) value);
	}
}