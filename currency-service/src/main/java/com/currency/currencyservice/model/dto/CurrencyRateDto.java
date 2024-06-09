package com.currency.currencyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRateDto {

    @JsonProperty("GOLD")
    private Integer gold;
    @JsonProperty("NAME_")
    private String name;
    @JsonProperty("CODE")
    private String code;
    @JsonProperty("RATIO")
    private String ratio;
    @JsonProperty("REVERSERATE")
    private String reverseRate;
    @JsonProperty("RATE")
    private String rate;
    @JsonProperty("CURR_DATE")
    private String date;
}
