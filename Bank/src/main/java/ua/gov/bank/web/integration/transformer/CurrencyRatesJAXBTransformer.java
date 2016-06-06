package ua.gov.bank.web.integration.transformer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ua.gov.bank.enums.OuterFormat;
import ua.gov.bank.model.CurrencyRate;
import ua.gov.bank.model.CurrencyRatesList;
@Component
public class CurrencyRatesJAXBTransformer implements CurrencyRatesTransformer {

	@Override
	public List<CurrencyRate> getRatesOfCurrencyFromPayload(byte[] data) {
		try {
/*			String dataToBeTransformed = new String (data,"UTF-8");
			System.out.println("DATA IN TRANSFORMER: "+dataToBeTransformed);*/
			JAXBContext jaxbContext = JAXBContext.newInstance(CurrencyRatesList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setSchema(null);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			spf.setValidating(false);
		
			XMLReader xr = (XMLReader) spf.newSAXParser().getXMLReader();
			SAXSource source = new SAXSource(xr, new InputSource(new ByteArrayInputStream(data)));
			CurrencyRatesList list = (CurrencyRatesList) jaxbUnmarshaller.unmarshal(source);
			return list.getRates();
		} catch (JAXBException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return new ArrayList<CurrencyRate>();
	}

	@Override
	public OuterFormat getFormat() {
		return OuterFormat.XML;
	}

}
