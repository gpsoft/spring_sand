package jp.dip.gpsoft.springsand.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueLoginIdValidator implements ConstraintValidator<UniqueLoginId, Object> {

	@Override
	public void initialize(UniqueLoginId annotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return true;
	}
}