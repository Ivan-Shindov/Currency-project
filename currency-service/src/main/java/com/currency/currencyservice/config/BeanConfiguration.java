package com.currency.currencyservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean(name = "bnbXmlMapper")
  public XmlMapper getXmlMapperBnb() {
    return XmlMapper.builder()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
  }

  @Bean
  public Gson gson() {
    return new GsonBuilder()
        .serializeNulls()
        .create();
  }
}
