package ua.gov.bank.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ua.gov.bank.web.util.converter.QueryParamConverter;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {
	@SuppressWarnings("rawtypes")
	Class<? extends QueryParamConverter> converter();
	String[] paramNames() default {};
}
