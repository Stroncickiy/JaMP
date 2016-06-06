package ua.gov.bank.model;


import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ua.gov.bank.web.integration.transformer.DateAdapter;
@XmlRootElement(namespace="http://bank.epam.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchPeriod {
	@XmlElement(name="start")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDate periodStart;
	@XmlElement(name="end")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDate periodEnd;

	
	public SearchPeriod(){
		
	}
	public SearchPeriod(LocalDate date) {
		this.periodStart = date;
		this.periodEnd = null;
	}

	public SearchPeriod(LocalDate start, LocalDate end) {
		this.periodStart = start;
		this.periodEnd = end;
	}

	public LocalDate getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(LocalDate periodStart) {
		this.periodStart = periodStart;
	}

	public LocalDate getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(LocalDate periodEnd) {
		this.periodEnd = periodEnd;
	}
	@Override
	public String toString() {
		return "SearchPeriod [periodStart=" + periodStart + ", periodEnd=" + periodEnd + "]";
	}
	
	

}
