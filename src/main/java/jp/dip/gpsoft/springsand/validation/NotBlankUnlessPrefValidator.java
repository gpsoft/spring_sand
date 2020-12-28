package jp.dip.gpsoft.springsand.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.dip.gpsoft.springsand.form.LakeForm;

public class NotBlankUnlessPrefValidator
		implements ConstraintValidator<NotBlankUnlessPref, LakeForm> {

	private NotBlankUnlessPref annotation;

	@Override
	public void initialize(NotBlankUnlessPref annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isValid(LakeForm value, ConstraintValidatorContext context) {
		if (value.isPref()) return true;
		if (value.getLocation() != null && !value.getLocation().equals("")) return true;
		context.buildConstraintViolationWithTemplate(annotation.message())
				.addPropertyNode("location")
				.addConstraintViolation();
		return false;
	}
}
