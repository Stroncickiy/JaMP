package ua.gov.bank.enums;

import ua.gov.bank.web.util.converter.UlrQueryConvertable;

public enum FormType implements UlrQueryConvertable {
	SEARCH_FORM_PERIOD("searchPeriodForm"),SEARCH_FORM_DATE("searchFormDate"),SEARCH_FORM_CUSTOM;
	
	private FormType(){
		
	}
	
	private FormType(String queryValue){
		this.queryValue =queryValue;
	}
	private String queryValue;
	
	public String getQueryValue(){
		return queryValue;
	}
}
