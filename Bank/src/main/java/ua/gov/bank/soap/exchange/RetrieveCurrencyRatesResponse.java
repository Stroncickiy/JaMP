package ua.gov.bank.soap.exchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ua.gov.bank.model.CurrencyRatesList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retrieveCurrencyRatesResponse", namespace = "http://bank.epam.com")
public class RetrieveCurrencyRatesResponse {
	@XmlElement(name="rates")
	private CurrencyRatesList ratesList;

	public CurrencyRatesList getRatesList() {
		return ratesList;
	}

	public void setRatesList(CurrencyRatesList ratesList) {
		this.ratesList = ratesList;
	}

	
}
