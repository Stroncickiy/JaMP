package ua.gov.bank.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ua.gov.bank.annotation.ConvertableToUrlQueryParams;
import ua.gov.bank.annotation.QueryParam;
import ua.gov.bank.enums.FormType;
import ua.gov.bank.enums.OuterFormat;
import ua.gov.bank.enums.TimeStep;
import ua.gov.bank.web.util.converter.SearchPeriodConverter;
import ua.gov.bank.web.util.converter.SimpleQueryParamConverter;

@XmlRootElement(name = "criteria")
@XmlAccessorType(XmlAccessType.FIELD)
@ConvertableToUrlQueryParams
public class SearchCriteria {

	@XmlElement(name = "formType")
	@QueryParam(converter = SimpleQueryParamConverter.class, paramNames = { "formType" })
	private FormType formType;

	@XmlElement(name = "timeStep")
	@QueryParam(converter = SimpleQueryParamConverter.class, paramNames = { "time_step" })
	private TimeStep timeStep;

	@XmlElement(name = "period")
	@QueryParam(converter = SearchPeriodConverter.class, paramNames = { "date", "periodStartTime", "periodEndTime" })
	private SearchPeriod searchPeriod;
	
	@XmlElementWrapper( name="currencies" )
	@XmlElement(name = "currency", required = false)
	private List<String> desiredCurrencies;

	@XmlTransient
	@QueryParam(converter = SimpleQueryParamConverter.class, paramNames = { "outer" })
	private OuterFormat outerFormat;

	@XmlElement(name="base")
	private String baseCurrency;

	public List<String> getDesiredCurrencies() {
		return desiredCurrencies;
	}

	public void setDesiredCurrencies(List<String> desiredCurrencies) {
		this.desiredCurrencies = desiredCurrencies;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}

	public TimeStep getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(TimeStep timeStep) {
		this.timeStep = timeStep;
	}

	public SearchPeriod getSearchPeriod() {
		return searchPeriod;
	}

	public void setSearchPeriod(SearchPeriod searchPeriod) {
		this.searchPeriod = searchPeriod;
	}

	public OuterFormat getOuterFormat() {
		return outerFormat;
	}

	public void setOuterFormat(OuterFormat outerFormat) {
		this.outerFormat = outerFormat;
	}

	@Override
	public String toString() {
		return "SearchCriteria [formType=" + formType + ", timeStep=" + timeStep + ", searchPeriod=" + searchPeriod
				+ ", desiredCurrencies=" + desiredCurrencies + ", outerFormat=" + outerFormat + ", baseCurrency="
				+ baseCurrency + "]";
	}
	
	
	

}
