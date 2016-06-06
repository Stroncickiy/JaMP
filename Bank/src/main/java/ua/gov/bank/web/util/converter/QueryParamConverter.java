package ua.gov.bank.web.util.converter;


public interface QueryParamConverter<T> {

	String buildQueryParam(T target, String... queryParamsNames);
}
