package jp.dip.gpsoft.springsand.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { UniqueLoginIdValidator.class })
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueLoginId {

	String message() default "{jp.dip.gpsoft.springsand.validation.UniqueLoginId.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List {
		UniqueLoginId[] value();
	}
}
