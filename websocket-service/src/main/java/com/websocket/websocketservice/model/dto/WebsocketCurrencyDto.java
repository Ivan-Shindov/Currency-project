package com.websocket.websocketservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "set")
@Getter
@Setter
public class WebsocketCurrencyDto {

  private String code;
  private Map<Integer,String> names;
  private BigDecimal rate;
  private BigDecimal reverseRate;
  private Integer ratio;
  private String date;
}
