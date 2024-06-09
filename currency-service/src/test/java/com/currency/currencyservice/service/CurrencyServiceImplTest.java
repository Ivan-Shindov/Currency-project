package com.currency.currencyservice.service;

import com.currency.currencyservice.mapper.WebsocketCurrencyDtoMapper;
import com.currency.currencyservice.model.dto.CurrencyRateBnb;
import com.currency.currencyservice.model.dto.WebsocketCurrencyDto;
import com.currency.currencyservice.repository.CurrencyRepository;
import com.currency.currencyservice.service.connector.BnbConnector;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

  private final BnbConnector bnbConnector = Mockito.mock(BnbConnector.class);
  private final CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);
  private final TransactionTemplate transactionTemplate = Mockito.mock(TransactionTemplate.class);
  private final WebsocketCurrencyDtoMapper websocketCurrencyDtoMapper =
      Mockito.mock(WebsocketCurrencyDtoMapper.class);
  private final SimpMessagingTemplate messagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
  private final Gson gson = Mockito.spy(Gson.class);

  private final CurrencyServiceImpl currencyService = new CurrencyServiceImpl(bnbConnector, currencyRepository,
      transactionTemplate,websocketCurrencyDtoMapper, messagingTemplate, gson);

  @Test
  void downloadCurrenciesFromBnbAndSaveInDb_NoCurrenciesReturned_ShouldNotSaveOrUpdate() {
    when(bnbConnector.getCurrencyRates()).thenReturn(Collections.emptyList());

    currencyService.downloadCurrenciesFromBnbAndSaveInDb();

    verify(currencyRepository, never()).saveAll(any());
    verify(messagingTemplate, never()).convertAndSend(anyString(), any(), any(MessageHeaders.class));
  }
}
