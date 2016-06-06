package ua.gov.bank.rest.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ua.gov.bank.enums.FormType;
import ua.gov.bank.enums.TimeStep;
import ua.gov.bank.model.CurrencyRatesList;
import ua.gov.bank.model.SearchCriteria;
import ua.gov.bank.model.SearchPeriod;
import ua.gov.bank.web.integration.CurrencyRatesDataProvider;

@RestController
public class CurrencyRatesController {
	@Autowired
	private CurrencyRatesDataProvider dataProvider;

	@RequestMapping(path = "/currencies", method = RequestMethod.POST)
	public CurrencyRatesList getCurrenciesByCriteria(@RequestBody SearchCriteria criteria) {
		return new CurrencyRatesList(dataProvider.getRatesOfCurrency(criteria));
	}

	@RequestMapping(path = "/example", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public SearchCriteria exampleCriteria() {
		SearchCriteria criteria = new SearchCriteria();
		ArrayList<String> currencyCodes = new ArrayList<>();
		currencyCodes.add("USD");
		currencyCodes.add("RUB");
		criteria.setDesiredCurrencies(currencyCodes);
		criteria.setFormType(FormType.SEARCH_FORM_DATE);
		criteria.setBaseCurrency("USD");
		SearchPeriod searchPeriod = new SearchPeriod(LocalDate.now());
		criteria.setSearchPeriod(searchPeriod);
		criteria.setTimeStep(TimeStep.MONTHLY);
		return criteria;
	}

}
