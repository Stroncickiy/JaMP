package ua.gov.bank.web.integration;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpDataRetriever {

	public static byte[] getDataFromUrl(String url) throws ClientProtocolException,
			IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();
		return IOUtils.toByteArray(entity.getContent());
	}
}
