package ua.gov.bank.web.integration;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.gov.bank.enums.FormType;
import ua.gov.bank.enums.TimeStep;
import ua.gov.bank.model.CurrencyRate;
import ua.gov.bank.model.SearchCriteria;
import ua.gov.bank.model.SearchPeriod;
import ua.gov.bank.web.integration.transformer.CurrencyRatesTransformer;

@Component
public class CurrencyRatesDataProvider {

	private static final String baseUrl = "http://www.bank.gov.ua/control/uk/curmetal/currency/search";
	private static final String defaultCurrency = "UAH";
	@Autowired
	private CurrencyRatesTransformer transformer;

	public List<CurrencyRate> getRatesOfCurrency(SearchCriteria criteria) {
		criteria.setOuterFormat(transformer.getFormat());
		List<CurrencyRate> fetchedData = new ArrayList<>();
		if (FormType.SEARCH_FORM_CUSTOM.equals(criteria.getFormType())) {
			System.out.println("Fetching data with custom criteria : " + criteria);
			fetchedData = retrieveDataWithCustomMode(criteria);
			if (criteria.getBaseCurrency()!=null&&!defaultCurrency.equals(criteria.getBaseCurrency())) {
				fetchedData = recalculateCurrencyRatesWithNewBaseCurrency(criteria.getBaseCurrency(), fetchedData);
			}
		} else {
			String targetDataUrl = baseUrl + "?" + QueryParamsStringBuilder.getQueryParamsFromObject(criteria);
			fetchedData = retriveCurrenciesByUrl(targetDataUrl, transformer);
			if (criteria.getBaseCurrency()!=null&&!defaultCurrency.equals(criteria.getBaseCurrency())) {
				fetchedData = recalculateCurrencyRatesWithNewBaseCurrency(criteria.getBaseCurrency(), fetchedData);
			}
		}

		return filterData(fetchedData, criteria.getDesiredCurrencies());
	}

	private List<CurrencyRate> filterData(List<CurrencyRate> originalData, List<String> desiredCurrencies) {
		List<CurrencyRate> resultList = new ArrayList<>(originalData);
		if (desiredCurrencies != null && desiredCurrencies.size() > 0) {
			resultList = new ArrayList<>();
			for (CurrencyRate rate : originalData) {
				if (desiredCurrencies.contains(rate.getLetterCode())) {
					resultList.add(rate);
				}
			}
		}
		return resultList;
	}

	private List<CurrencyRate> recalculateCurrencyRatesWithNewBaseCurrency(String baseCurrency,
			List<CurrencyRate> originalData) {

		CurrencyRate baseCurrencyObject = findCurrencyByLetterCode(originalData, baseCurrency);
		baseCurrencyObject.setExchangeRate(1/(baseCurrencyObject.getExchangeRate()/baseCurrencyObject.getNumberOfUnits())); // exchange rate of currency to current base  for 1 unit
		baseCurrencyObject.setNumberOfUnits(1);
		
		ArrayList<CurrencyRate> rebasedCurrencies = new ArrayList<CurrencyRate>();
		for (CurrencyRate currencyRate : originalData) {
			if (!baseCurrency.equals(currencyRate.getLetterCode())) {
				
				currencyRate.setExchangeRate(1/(currencyRate.getExchangeRate()/currencyRate.getNumberOfUnits())); 
				currencyRate.setNumberOfUnits(1);
				
				currencyRate.setExchangeRate(  currencyRate.getExchangeRate() / baseCurrencyObject.getExchangeRate() );
				rebasedCurrencies.add(currencyRate);
			}
		}
		return originalData;
	}

	private List<CurrencyRate> retriveCurrenciesByUrl(String targetDataUrl, CurrencyRatesTransformer transformer) {
		System.out.println("Fetching data by following url : " + targetDataUrl);
		byte[] data = null;
		try {
			data = HttpDataRetriever.getDataFromUrl(targetDataUrl);
		} catch (IOException e) {
			throw new RuntimeException("Unable to retrieve data by following URL: " + targetDataUrl);
		}
		return transformer.getRatesOfCurrencyFromPayload(data);

	}

	private List<CurrencyRate> retrieveDataWithCustomMode(SearchCriteria criteria) {
		List<CurrencyRate> resultList = new ArrayList<>();
		switch (criteria.getTimeStep()) {
		case MONTH_FIRST_DAY:
			LocalDate firstDayOfMonth = getFirstDayOfMonth(criteria.getSearchPeriod().getPeriodStart());
			resultList = fetchDataByFixedDate(firstDayOfMonth);
			break;
		case MONTH_AVERAGE:
			LocalDate monthStart = getFirstDayOfMonth(criteria.getSearchPeriod().getPeriodStart());
			LocalDate monthEnd = getLastDayOfMonth(criteria.getSearchPeriod().getPeriodStart());
			resultList = fetchAverageDataForDateRange(monthStart, monthEnd);
			break;
		case WEEK_FIRST_DAY:
			LocalDate firstDayOfWeek = getFirstDayOfWeek(criteria.getSearchPeriod().getPeriodStart());
			resultList = fetchDataByFixedDate(firstDayOfWeek);
			break;
		case WEEK_AVERAGE:
			LocalDate weekStart = getFirstDayOfWeek(criteria.getSearchPeriod().getPeriodStart());
			LocalDate weekEnd = getLastDayOfWeek(criteria.getSearchPeriod().getPeriodStart());
			resultList = fetchAverageDataForDateRange(weekStart, weekEnd);
			break;

		default:
			break;
		}
		return resultList;
	}

	private List<CurrencyRate> fetchAverageDataForDateRange(LocalDate rangeStart, LocalDate rangeEnd) {
		long dateRangeLength = getDateRangeLength(rangeStart, rangeEnd);
		Map<String, List<Float>> currencyRates = new HashMap<>();

		for (int i = 0; i < dateRangeLength; i++) {
			List<CurrencyRate> thisDateData = fetchDataByFixedDate(shiftDateBy(rangeStart, i));
			for (CurrencyRate currencyRate : thisDateData) {
				String letterCode = currencyRate.getLetterCode();
				if (currencyRates.containsKey(letterCode)) {
					List<Float> list = currencyRates.get(currencyRate.getLetterCode());
					list.add(findCurrencyByLetterCode(thisDateData, letterCode).getExchangeRate());
				} else {
					ArrayList<Float> initialList = new ArrayList<>();
					initialList.add(currencyRate.getExchangeRate());
					currencyRates.put(currencyRate.getLetterCode(), initialList);
				}
			}
		}
		return calculateAverageExchangeRates(fetchDataByFixedDate(rangeStart), currencyRates);
	}

	private CurrencyRate findCurrencyByLetterCode(List<CurrencyRate> thisDateData, String letterCode) {
		for (CurrencyRate currencyRate : thisDateData) {
			if (letterCode.equals(currencyRate.getLetterCode())) {
				return currencyRate;
			}
		}
		return null;
	}

	private List<CurrencyRate> calculateAverageExchangeRates(List<CurrencyRate> originalData,
			Map<String, List<Float>> currencyRates) {
		for (CurrencyRate currencyRate : originalData) {
			currencyRate.setExchangeRate(calculateAverageExchangeRate(currencyRates.get(currencyRate.getLetterCode())));
		}
		return originalData;
	}

	private float calculateAverageExchangeRate(List<Float> list) {
		float sum = 0f;
		for (Float value : list) {
			sum += value;
		}
		return sum / list.size();
	}

	private LocalDate shiftDateBy(LocalDate rangeStart, int days) {
		return rangeStart.plusDays(days);
	}

	private long getDateRangeLength(LocalDate rangeStart, LocalDate rangeEnd) {
		return ChronoUnit.DAYS.between(rangeStart, rangeEnd);
	}

	private List<CurrencyRate> fetchDataByFixedDate(LocalDate targetDate) {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setFormType(FormType.SEARCH_FORM_DATE);
		criteria.setTimeStep(TimeStep.DAILY);
		criteria.setSearchPeriod(new SearchPeriod(targetDate));
		criteria.setOuterFormat(transformer.getFormat());
		String targetDataUrl = baseUrl + "?" + QueryParamsStringBuilder.getQueryParamsFromObject(criteria);
		return retriveCurrenciesByUrl(targetDataUrl, transformer);
	}

	private LocalDate getFirstDayOfMonth(LocalDate localDate) {
		return localDate.withDayOfMonth(1);
	}

	private LocalDate getLastDayOfMonth(LocalDate localeDate) {
		return localeDate.withDayOfMonth(localeDate.lengthOfMonth());
	}

	private LocalDate getFirstDayOfWeek(LocalDate localDate) {
		return localDate.with(DayOfWeek.MONDAY);
	}

	private LocalDate getLastDayOfWeek(LocalDate localeDate) {
		return localeDate.with(DayOfWeek.SUNDAY);
	}

}
