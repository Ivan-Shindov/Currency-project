package com.currency.currencyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyNameDtoBg {

  @JsonProperty("NAME_")
  private String bgName;
  @JsonProperty("CODE")
  private String code;
}
