package ua.gov.bank.enums;

import ua.gov.bank.web.util.converter.UlrQueryConvertable;

public enum OuterFormat implements UlrQueryConvertable {
	XML, XSL, TABLE;

	@Override
	public String getQueryValue() {
		return name().toLowerCase();
	}
	
	
}
