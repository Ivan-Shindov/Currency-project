package com.currency.currencyservice.mapper;

import com.currency.currencyservice.model.dto.WebsocketCurrencyDto;
import com.currency.currencyservice.model.entity.CurrencyEntity;
import com.currency.currencyservice.model.entity.CurrencyNameEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class WebsocketCurrencyDtoMapperImpl implements WebsocketCurrencyDtoMapper {

  public WebsocketCurrencyDto mapEntityToWebsocketDto(CurrencyEntity currencyEntity) {

    return WebsocketCurrencyDto.builder()
        .setCode(currencyEntity.getCode())
        .setDate(String.valueOf(currencyEntity.getDate()))
        .setRate(currencyEntity.getRate())
        .setRatio(currencyEntity.getRatio())
        .setReverseRate(currencyEntity.getReverseRate())
        .setNames(mapCurrencyNames(currencyEntity.getNames()))
        .build();
  }

  private Map<Integer, String> mapCurrencyNames(Set<CurrencyNameEntity> names) {

    Map<Integer, String> mapToReturn = new HashMap<>();
    for (CurrencyNameEntity currencyNameEntity : names) {
      mapToReturn.putIfAbsent(currencyNameEntity.getLanguage(), currencyNameEntity.getName());
    }
    return mapToReturn;
  }

}
