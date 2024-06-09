package com.websocket.websocketservice.mapper;

import com.websocket.websocketservice.model.CurrencyRateEntity;
import com.websocket.websocketservice.model.CurrencyRateNameEntity;
import com.websocket.websocketservice.model.dto.WebsocketCurrencyDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class WebsocketDtoMapperImpl implements WebsocketDtoMapper {

  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public CurrencyRateEntity mapWebsocketDtoToCurrencyRateEntity(WebsocketCurrencyDto websocketCurrencyDto) {

    CurrencyRateEntity currencyRateEntity = new CurrencyRateEntity();
    currencyRateEntity.setCode(websocketCurrencyDto.getCode());
    currencyRateEntity.setDate(LocalDate.parse(websocketCurrencyDto.getDate(), DATE_TIME_FORMATTER));
    currencyRateEntity.setRate(websocketCurrencyDto.getRate());
    currencyRateEntity.setRatio(websocketCurrencyDto.getRatio());
    currencyRateEntity.setReverseRate(websocketCurrencyDto.getReverseRate());
    return currencyRateEntity;
  }

  public Set<CurrencyRateNameEntity> mapToCurrencyRateNames(Map<Integer, String> names) {

    Set<CurrencyRateNameEntity> currencyRateNameEntities = new HashSet<>();
    names.forEach((key, value) -> {
      CurrencyRateNameEntity currencyRateNameEntity = new CurrencyRateNameEntity();
      currencyRateNameEntity.setName(value);
      currencyRateNameEntity.setLanguage(key);
      currencyRateNameEntities.add(currencyRateNameEntity);
    });

    return currencyRateNameEntities;
  }
}
