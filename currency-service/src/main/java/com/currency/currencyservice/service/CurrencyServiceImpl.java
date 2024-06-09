package com.currency.currencyservice.service;

import com.currency.currencyservice.enumerator.Language;
import com.currency.currencyservice.mapper.WebsocketCurrencyDtoMapper;
import com.currency.currencyservice.model.dto.CurrencyRateBnb;
import com.currency.currencyservice.model.dto.WebsocketCurrencyDto;
import com.currency.currencyservice.model.entity.ChronologicalExchangeRateEntity;
import com.currency.currencyservice.model.entity.CurrencyEntity;
import com.currency.currencyservice.model.entity.CurrencyNameEntity;
import com.currency.currencyservice.repository.CurrencyRepository;
import com.currency.currencyservice.service.connector.BnbConnector;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

  private final BnbConnector bnbConnector;
  private final CurrencyRepository currencyRepository;
  private final TransactionTemplate transactionTemplate;
  private final WebsocketCurrencyDtoMapper websocketCurrencyDtoMapper;
  private final SimpMessagingTemplate messagingTemplate;
  private final Gson gson;

  private static final String BNB = "BNB";
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");


  @Override
  public void downloadCurrenciesFromBnbAndSaveInDb() {
    List<CurrencyRateBnb> currencyRates = bnbConnector.getCurrencyRates();
    // download bnb content
    List<WebsocketCurrencyDto> websocketCurrencies = new ArrayList<>();
    transactionTemplate.executeWithoutResult(status -> {
      Map<String, CurrencyEntity> currencyRateEntities = new HashMap<>();
      List<CurrencyRateBnb> existCurrencies = new ArrayList<>();

      for (CurrencyRateBnb currencyRate : currencyRates) {
        // check if currency not exists, create and add chronological entity
        if (!isCurrencyRateExists(currencyRate.getCode())) {
          CurrencyEntity rateEntity = buildCurrencyEntity(currencyRate);
          buildChronologicalExchangeRateEntityAndAddToCurrency(currencyRate, rateEntity);
          currencyRateEntities.put(currencyRate.getCode(), rateEntity);
        } else {
          existCurrencies.add(currencyRate);
        }
      }

      if (!currencyRateEntities.isEmpty()) {
        // handle when new currency is added from BNB
        currencyRepository.saveAll(currencyRateEntities.values());
        currencyRateEntities.values().forEach(currency -> {
          WebsocketCurrencyDto websocketCurrencyDto =
              websocketCurrencyDtoMapper.mapEntityToWebsocketDto(currency);
          websocketCurrencies.add(websocketCurrencyDto);
        });
      } else {
        // currency exists and should compare rates to these from BNB
        Map<String, CurrencyEntity> currenciesByCodes = getCurrenciesByExistingCurrencyCodes(existCurrencies);
        for (CurrencyRateBnb currencyRate : existCurrencies) {
          CurrencyEntity currencyEntity = currenciesByCodes.get(currencyRate.getCode());
          BigDecimal rateBnb = getRate(currencyRate.getRate());
          BigDecimal reverseRateBnb = getReverseRate(currencyRate.getReverseRate());

          List<Boolean> shouldUpdate = shouldUpdateCurrencyEntity(currencyEntity, rateBnb, reverseRateBnb,
              currencyRate.getDate());
          CurrencyEntity updatedCurrencyEntity = updateCurrencyEntity(currencyRate, currencyEntity,
              rateBnb, reverseRateBnb, shouldUpdate);

          if (updatedCurrencyEntity != null) {
            WebsocketCurrencyDto websocketCurrencyDto =
                websocketCurrencyDtoMapper.mapEntityToWebsocketDto(updatedCurrencyEntity);
            websocketCurrencies.add(websocketCurrencyDto);
          }
        }
      }
    });

    if (!websocketCurrencies.isEmpty()) {
      for (WebsocketCurrencyDto websocketCurrency : websocketCurrencies) {
        String json = gson.toJson(websocketCurrency);
        messagingTemplate.convertAndSend("/topic/messages", json, createMessageHeaders());
      }
    }
  }

  private MessageHeaders createMessageHeaders() {
    return new MessageHeaders(Map.of(
        MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON
    ));
  }

  private HashMap<String, CurrencyEntity> getCurrenciesByExistingCurrencyCodes(
      List<CurrencyRateBnb> existCurrencies) {

    return currencyRepository.getCurrenciesByCodes(
        existCurrencies.stream()
            .map(CurrencyRateBnb::getCode)
            .collect(Collectors.toList()))
        .stream()
            .collect(Collectors.toMap(
                CurrencyEntity::getCode, currencyEntity -> currencyEntity,
                (oldValue, newValue) -> oldValue, HashMap::new));
  }

  private List<Boolean> shouldUpdateCurrencyEntity(CurrencyEntity currencyEntity, BigDecimal newRate,
      BigDecimal newReverseRate, String currDateAsString) {

    boolean isRateChanged = safeBigDecimalCompare(newRate, currencyEntity.getRate());
    boolean isReverseRateChanged = safeBigDecimalCompare(newReverseRate, currencyEntity.getReverseRate());
    LocalDate dateFromCurrencyDto = LocalDate.parse(currDateAsString, DATE_TIME_FORMATTER);

    boolean isDateChanged = dateFromCurrencyDto.isAfter(currencyEntity.getDate());

    return List.of(isRateChanged, isReverseRateChanged, isDateChanged);
  }

  private boolean safeBigDecimalCompare(BigDecimal newValue, BigDecimal oldValue) {
    if (newValue == null && oldValue == null) {
      return false; // Both are null, consider them equal.
    }

    if (newValue == null || oldValue == null) {
      return true; // One is null and the other is not, consider them different.
    }

    return newValue.compareTo(oldValue) != 0; // Neither are null, use compareTo.
  }

  private BigDecimal getRate(String rate) {
    return rate != null ? new BigDecimal(rate) : null;
  }

  private CurrencyEntity updateCurrencyEntity(CurrencyRateBnb currencyRate, CurrencyEntity currencyEntity,
      BigDecimal newRate, BigDecimal newReverseRate, List<Boolean> shouldUpdate) {

    boolean isRateChanged = shouldUpdate.get(0);
    boolean isReverseRateChanged = shouldUpdate.get(1);
    boolean isDateChanged = shouldUpdate.get(2);

    if (isRateChanged || isReverseRateChanged || isDateChanged) {
      currencyEntity.setRate(newRate);
      currencyEntity.setReverseRate(newReverseRate);
      currencyEntity.setRatio(getRatio(currencyRate));
      currencyEntity.setUpdatedOn(Instant.now());
      currencyEntity.setDate(LocalDate.parse(currencyRate.getDate(), DATE_TIME_FORMATTER));

      // Assume this method creates a new ChronologicalExchangeRateEntity and adds it
      buildChronologicalExchangeRateEntityAndAddToCurrency(currencyRate, currencyEntity);
      return currencyRepository.save(currencyEntity);
    }
    return null;
  }

  private void buildChronologicalExchangeRateEntityAndAddToCurrency(CurrencyRateBnb currencyRate,
      CurrencyEntity currencyEntity) {

    ChronologicalExchangeRateEntity exchangeRateEntity = ChronologicalExchangeRateEntity.builder()
        .setCreatedOn(Instant.now())
        .setRate(getRate(currencyRate.getRate()))
        .setReverseRate(getReverseRate(currencyRate.getReverseRate()))
        .build();

    currencyEntity.addChronologicalExchangeRateEntity(exchangeRateEntity);
  }

  private boolean isCurrencyRateExists(String currencyRateCode) {
    return currencyRepository.isCurrencyExistsByCode(currencyRateCode);
  }

  private CurrencyEntity buildCurrencyEntity(CurrencyRateBnb currencyRate) {
    List<CurrencyNameEntity> currencyNameEntities = Arrays.stream(Language.values())
        .map(language -> {
          CurrencyNameEntity nameEntity = new CurrencyNameEntity();
          nameEntity.setCreatedOn(Instant.now());
          nameEntity.setLanguage(language.getId());
          nameEntity.setName(currencyRate.getNames().get(language.getId()));
          return nameEntity;
        })
        .collect(Collectors.toList());

    CurrencyEntity rateEntity = new CurrencyEntity();
    rateEntity.setRate(getRate(currencyRate.getRate()));
    rateEntity.setReverseRate(getReverseRate(currencyRate.getReverseRate()));
    rateEntity.setSource(BNB);
    rateEntity.setCreatedBy(BNB);
    rateEntity.setCode(currencyRate.getCode());
    rateEntity.setRatio(getRatio(currencyRate));
    rateEntity.setCreatedOn(Instant.now());
    rateEntity.setDate(LocalDate.parse(currencyRate.getDate(),DATE_TIME_FORMATTER));

    rateEntity.addNames(currencyNameEntities);

    return rateEntity;
  }

  private Integer getRatio(CurrencyRateBnb currencyRate) {
    return currencyRate.getRatio() != null
        ? Integer.parseInt(currencyRate.getRatio())
        : null;
  }

  private BigDecimal getReverseRate(String reverseRate) {
    return reverseRate != null
        ? new BigDecimal(reverseRate)
        : null;
  }
}
