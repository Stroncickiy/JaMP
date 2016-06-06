package ua.gov.bank.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ua.gov.bank.web.integration.transformer.DateAdapter;

@XmlRootElement(name = "currency")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyRate {
	@XmlElement(name = "date")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDate date;
	@XmlElement(name = "digital_code")
	private int digitalCode;
	@XmlElement(name = "letter_code")
	private String letterCode;
	@XmlElement(name = "number_of_units")
	private int numberOfUnits;
	@XmlElement(name = "currency_name")
	private String currencyName;
	@XmlElement(name = "exchange_rate")
	private float exchangeRate;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getDigitalCode() {
		return digitalCode;
	}

	public void setDigitalCode(int digitalCode) {
		this.digitalCode = digitalCode;
	}

	public String getLetterCode() {
		return letterCode;
	}

	public void setLetterCode(String letterCode) {
		this.letterCode = letterCode;
	}

	public int getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(int numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public float getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
