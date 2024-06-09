package com.currency.currencyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CurrencyRateBgWrapperDto {

  @JacksonXmlElementWrapper(useWrapping = false)
  @JsonProperty("ROW")
  private List<CurrencyNameDtoBg> currencyBgNames = new ArrayList<>();

}
