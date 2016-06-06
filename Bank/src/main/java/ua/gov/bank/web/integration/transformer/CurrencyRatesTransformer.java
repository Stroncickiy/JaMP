package ua.gov.bank.web.integration.transformer;

import java.util.List;

import ua.gov.bank.enums.OuterFormat;
import ua.gov.bank.model.CurrencyRate;

public interface CurrencyRatesTransformer {
	List<CurrencyRate> getRatesOfCurrencyFromPayload(byte[] data);
	OuterFormat getFormat();
}
