package com.websocket.websocketservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketClientRunner implements CommandLineRunner {

  private static final String WS_STOMP_ENDPOINT = "ws://localhost:8080/stomp-endpoint";
  private final WebsocketConnectionService webSocketConnectionService;

  @Override
  public void run(String... args) {
    webSocketConnectionService.connect(WS_STOMP_ENDPOINT);
    log.info("WebsocketConnectionService - connectStompSession() was invoked");
  }
}
