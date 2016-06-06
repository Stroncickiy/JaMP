REST  endpoint located by following address and consumes payload e.g.
{ 
  "formType": "SEARCH_FORM_CUSTOM",  // options: SEARCH_FORM_PERIOD   SEARCH_FORM_DATE   SEARCH_FORM_CUSTOM;
  "timeStep": "WEEK_AVERAGE",     // options: 	MONTHLY, DAILY - default time steps (can be used with SEARCH_FORM_PERIOD and SEARCH_FORM_DATE);  WEEK_FIRST_DAY, WEEK_AVERAGE, MONTH_FIRST_DAY, MONTH_AVERAGE custom time steps, can be used only when formType is SEARCH_FORM_CUSTOM
  "searchPeriod": {
    "periodStart": "2016-05-03"   // search period in format yyyy-MM-dd  ( when formType is SEARCH_FORM_PERIOD you should also specify "periodStart" )
  },
  "desiredCurrencies": [   // optional, can be used when you want to fetch only specific currencies 
    "EUR"
  ],
  "baseCurrency": "USD"  // optional, can be used if you want to change base currency
}

SOAP endpoint located by address http://localhost:8080/ws/currenciesService 
wsdl for this endpoint http://localhost:8080/ws/CurrenciesService.wsdl 
and consumes following payload:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:bank="http://bank.epam.com">
   <soapenv:Header/>
   <soapenv:Body>
      <bank:retrieveCurrencyRatesRequest>
         <bank:criteria>
            <formType>SEARCH_FORM_CUSTOM</formType>
            <timeStep>WEEK_AVERAGE</timeStep>
            <period>
               <start>01.05.2016</start>
            </period>
            <currencies>
               <currency>EUR</currency>
               <currency>RUB</currency>
            </currencies>
            <base>USD</base>
         </bank:criteria>
      </bank:retrieveCurrencyRatesRequest>
   </soapenv:Body>
</soapenv:Envelope>