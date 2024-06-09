package com.currency.currencyservice.web;

import org.springframework.http.ResponseEntity;

public interface CurrencyController {

  ResponseEntity<Void> downloadCurrenciesFromBnbAndSave();
}
