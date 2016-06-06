package ua.gov.bank.web.util.converter;

import java.time.format.DateTimeFormatter;

import ua.gov.bank.model.SearchPeriod;

public class SearchPeriodConverter implements QueryParamConverter<SearchPeriod> {

	private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@Override
	public String buildQueryParam(SearchPeriod target, String... queryParamsNames) {
		if (target.getPeriodEnd() == null) {
			return queryParamsNames[0] + "=" + dateFormat.format(target.getPeriodStart());
		} else {
			return queryParamsNames[1] + "=" + dateFormat.format(target.getPeriodStart()) + "&" + queryParamsNames[2]
					+ "=" + dateFormat.format(target.getPeriodEnd());
		}
	}

}
