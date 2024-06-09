package com.currency.currencyservice.mapper;

import com.currency.currencyservice.model.dto.WebsocketCurrencyDto;
import com.currency.currencyservice.model.entity.CurrencyEntity;

public interface WebsocketCurrencyDtoMapper {

  WebsocketCurrencyDto mapEntityToWebsocketDto(CurrencyEntity currencyEntity);
}
