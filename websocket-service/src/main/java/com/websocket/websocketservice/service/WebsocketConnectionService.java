package com.websocket.websocketservice.service;

import com.websocket.websocketservice.config.MyStompSessionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebsocketConnectionService {

  private final WebSocketStompClient stompClient;
  private final MyStompSessionHandler sessionHandler;

  public void connect(String url) {
    stompClient.connectAsync(url, sessionHandler);
    log.info("Connected to WebSocket server at {}", url);
  }
}
