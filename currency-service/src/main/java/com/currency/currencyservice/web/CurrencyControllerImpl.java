package com.currency.currencyservice.web;

import com.currency.currencyservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyControllerImpl implements CurrencyController {

  private final CurrencyService currencyService;

  @Override
  @PostMapping("download-currencies")
  // @MessageMapping("/send")
  @SendTo("/topic/messages")
  public ResponseEntity<Void> downloadCurrenciesFromBnbAndSave() {
    currencyService.downloadCurrenciesFromBnbAndSaveInDb();
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}