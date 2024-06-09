package com.websocket.websocketservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.websocketservice.model.dto.WebsocketCurrencyDto;
import com.websocket.websocketservice.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

  private final CurrencyRateService currencyRateService;

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    session.subscribe("/topic/messages", this);
    log.info("Connected! Headers: " + connectedHeaders);
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    WebsocketCurrencyDto websocketCurrencyDto = null;
    try {
      websocketCurrencyDto = new ObjectMapper().readValue(payload.toString(), WebsocketCurrencyDto.class);
    } catch (JsonProcessingException e) {
      log.error("Error occurred {}", e.getMessage());
    }
    if (websocketCurrencyDto == null) {
      log.warn("Received websocket message was null");
      return;
    }

    currencyRateService.upsertWebsocketCurrencyDto(websocketCurrencyDto);
    log.info("Received websocket message value: {}", websocketCurrencyDto.toString());
  }


  @Override
  public Type getPayloadType(StompHeaders headers) {
    if (("text/plain".equals(headers.getFirst("content-type")))
        || ("text/plain;charset=UTF-8".equals(headers.getFirst("content-type")))) {
      log.info("Received plain text message: {}", headers);
    } else {
      log.info("Received application json message: {}", headers);
    }
    return String.class;
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers,
      byte[] payload, Throwable exception) {
    log.error("Error occurred {}", exception.getMessage());
  }
}