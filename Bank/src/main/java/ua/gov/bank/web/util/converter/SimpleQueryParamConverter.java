package ua.gov.bank.web.util.converter;

public class SimpleQueryParamConverter implements
		QueryParamConverter<UlrQueryConvertable> {

	@Override
	public String buildQueryParam(UlrQueryConvertable target,
			String... queryParamNames) {
		return queryParamNames[0] +"="+ target.getQueryValue();
	}

}
