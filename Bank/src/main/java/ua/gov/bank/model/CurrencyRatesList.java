package ua.gov.bank.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "currencies")
public class CurrencyRatesList {
	@XmlElement(name="currency")
	private List<CurrencyRate> currencies;


	public CurrencyRatesList(){
		
	}
	
	public CurrencyRatesList(List<CurrencyRate> ratesOfCurrencies) {
		this.currencies = ratesOfCurrencies;
	}

	public List<CurrencyRate> getRates() {
		return currencies;
	}

	public void setRates(List<CurrencyRate> rates) {
		this.currencies = rates;
	}

}
