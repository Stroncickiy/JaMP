package ua.gov.bank.soap.exchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ua.gov.bank.model.SearchCriteria;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retrieveCurrencyRatesRequest", namespace = "http://bank.epam.com")
public class RetrieveCurrencyRatesRequest {
	@XmlElement(name="criteria",namespace="http://bank.epam.com")
	private SearchCriteria criteria;

	public SearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(SearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	
}
