package ua.gov.bank.app;


import org.springframework.boot.builder.SpringApplicationBuilder;

import ua.gov.bank.configuration.DispatcherServletInitializer;

public class Application {

    public static void main(String[] args) {
    	System.setProperty("javax.xml.accessExternalSchema", "all");
        new SpringApplicationBuilder(DispatcherServletInitializer.class)
                .run(args);
    }


}
