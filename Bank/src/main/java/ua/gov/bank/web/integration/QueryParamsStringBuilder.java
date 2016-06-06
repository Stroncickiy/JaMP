package ua.gov.bank.web.integration;

import java.lang.reflect.Field;

import ua.gov.bank.annotation.ConvertableToUrlQueryParams;
import ua.gov.bank.annotation.QueryParam;
import ua.gov.bank.web.util.converter.QueryParamConverter;

public class QueryParamsStringBuilder {

	@SuppressWarnings("unchecked")
	public static String getQueryParamsFromObject(Object convertableObject) {
		if (convertableObject.getClass().getAnnotationsByType(ConvertableToUrlQueryParams.class).length == 0) {
			throw new UnsupportedOperationException(
					"object must be annotated with  @ConvertableToUrlQueryParams, otherwise it treated as unsupported ");
		}
		StringBuilder queryParamsStringBuilder = new StringBuilder();

		Field[] fields = convertableObject.getClass().getDeclaredFields();
		for (Field field : fields) {
			QueryParam queryParamAnnotation = field.getAnnotation(QueryParam.class);
			if (queryParamAnnotation != null) {

				@SuppressWarnings("rawtypes")
				Class<? extends QueryParamConverter> converterClass = queryParamAnnotation.converter();
				QueryParamConverter<Object> converter;
				Object fieldValue = null;
				try {
					converter = converterClass.newInstance();
					field.setAccessible(true);
					fieldValue = field.get(convertableObject);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException("unable to instantiate converter class for field " + field.getName()
							+ ", make sure it has default constructor");
				}

				String[] paramNames = queryParamAnnotation.paramNames();
				String resultQueryForField = null;

				resultQueryForField = converter.buildQueryParam(fieldValue, paramNames);

				if (resultQueryForField != null) {
					queryParamsStringBuilder.append(resultQueryForField).append("&");
				}

			}
		}
		queryParamsStringBuilder.setLength(queryParamsStringBuilder.length() - 1);
		return queryParamsStringBuilder.toString();

	}
}
