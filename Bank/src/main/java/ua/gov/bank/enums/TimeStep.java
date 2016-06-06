package ua.gov.bank.enums;

import ua.gov.bank.web.util.converter.UlrQueryConvertable;

public enum TimeStep implements UlrQueryConvertable {
	MONTHLY("monthly"), DAILY("daily"), WEEK_FIRST_DAY, WEEK_AVERAGE, MONTH_FIRST_DAY, MONTH_AVERAGE;

	private TimeStep() {

	}
	private TimeStep(String queryValue) {
		this.queryValue = queryValue;
	}

	private String queryValue;

	public String getQueryValue() {
		return queryValue;
	}
}
