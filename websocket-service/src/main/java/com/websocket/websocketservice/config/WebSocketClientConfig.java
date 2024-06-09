package com.websocket.websocketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class WebSocketClientConfig {

  @Bean
  public WebSocketStompClient webSocketStompClient() {
    List<org.springframework.messaging.converter.MessageConverter> converters = new ArrayList<>();
    converters.add(new MappingJackson2MessageConverter());
    converters.add(new StringMessageConverter());

    CompositeMessageConverter compositeConverter = new CompositeMessageConverter(converters);

    WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
    stompClient.setMessageConverter(compositeConverter);
    return stompClient;
  }
}
