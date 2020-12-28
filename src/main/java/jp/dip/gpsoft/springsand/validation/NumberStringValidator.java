package jp.dip.gpsoft.springsand.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberStringValidator implements ConstraintValidator<NumberString, Object> {

	@Override
	public void initialize(NumberString annotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (!(value instanceof String)) return false;
		String v = (String) value;
		if (v.equals("")) return true;
		return v.matches("^\\d+(\\.\\d+)?$");
	}
}