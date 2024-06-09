package com.currency.currencyservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(setterPrefix = "set", toBuilder = true)
@AllArgsConstructor
public class CurrencyRateBnb {

  private Integer gold;
  private final Map<Integer, String> names = new HashMap<>();
  private String code;
  private String ratio;
  private String reverseRate;
  private String rate;
  private String date;
}
