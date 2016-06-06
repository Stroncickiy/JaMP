package ua.gov.bank.soap;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ua.gov.bank.model.CurrencyRate;
import ua.gov.bank.model.CurrencyRatesList;
import ua.gov.bank.soap.exchange.RetrieveCurrencyRatesRequest;
import ua.gov.bank.soap.exchange.RetrieveCurrencyRatesResponse;
import ua.gov.bank.web.integration.CurrencyRatesDataProvider;

@Endpoint
public class CurrenciesWSEndpoint {
    private static final String NAMESPACE_URI = "http://bank.epam.com";
	@Autowired
	private CurrencyRatesDataProvider dataProvider;
    
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "retrieveCurrencyRatesRequest")
    @ResponsePayload
    public JAXBElement<RetrieveCurrencyRatesResponse> getCurrencyRatesByCrieteria(@RequestPayload JAXBElement<RetrieveCurrencyRatesRequest> ratesRequest){
    	List<CurrencyRate> ratesOfCurrency = dataProvider.getRatesOfCurrency(ratesRequest.getValue().getCriteria());
    	CurrencyRatesList currencyRatesList = new CurrencyRatesList(ratesOfCurrency);
    	RetrieveCurrencyRatesResponse response = new RetrieveCurrencyRatesResponse();
    	response.setRatesList(currencyRatesList);
		return new JAXBElement<RetrieveCurrencyRatesResponse>(new QName(NAMESPACE_URI, "retrieveCurrencyRatesResponse"), RetrieveCurrencyRatesResponse.class, response);
    	
    }

}
