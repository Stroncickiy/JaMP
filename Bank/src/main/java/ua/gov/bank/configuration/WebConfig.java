package ua.gov.bank.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "ua.gov.*")
@EnableWs
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean(name = "OBJECT_MAPPER_BEAN")
	public ObjectMapper jsonObjectMapper() {
	    return Jackson2ObjectMapperBuilder.json()
	            .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
	            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
	            .modules(new JSR310Module())
	            .build();
	}
	// WS
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "CurrenciesService")
	public DefaultWsdl11Definition currenciesWsdlDef(@Qualifier("currenciesSchema") SimpleXsdSchema simpleXsdSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CurrenciesService");
		wsdl11Definition.setLocationUri("/ws/currenciesService");
		wsdl11Definition.setTargetNamespace("http://bank.epam.com");
		wsdl11Definition.setSchema(simpleXsdSchema);
		return wsdl11Definition;
	}

	@Qualifier("currenciesSchema")
	@Bean
	public SimpleXsdSchema usersSchema() {
		return new SimpleXsdSchema(new ClassPathResource("xsd/schema.xsd"));
	}

}