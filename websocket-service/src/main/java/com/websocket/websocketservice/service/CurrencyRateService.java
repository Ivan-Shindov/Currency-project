package com.websocket.websocketservice.service;

import com.websocket.websocketservice.model.dto.WebsocketCurrencyDto;

public interface CurrencyRateService {

  void upsertWebsocketCurrencyDto(WebsocketCurrencyDto websocketCurrencyDto);
}
