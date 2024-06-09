package com.websocket.websocketservice.mapper;

import com.websocket.websocketservice.model.CurrencyRateEntity;
import com.websocket.websocketservice.model.CurrencyRateNameEntity;
import com.websocket.websocketservice.model.dto.WebsocketCurrencyDto;

import java.util.Map;
import java.util.Set;

public interface WebsocketDtoMapper {

  CurrencyRateEntity mapWebsocketDtoToCurrencyRateEntity(WebsocketCurrencyDto websocketCurrencyDto);

  Set<CurrencyRateNameEntity> mapToCurrencyRateNames(Map<Integer, String> names);

  }
