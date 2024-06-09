package com.websocket.websocketservice.service;

import com.websocket.websocketservice.mapper.WebsocketDtoMapper;
import com.websocket.websocketservice.model.CurrencyRateEntity;
import com.websocket.websocketservice.model.CurrencyRateNameEntity;
import com.websocket.websocketservice.model.dto.WebsocketCurrencyDto;
import com.websocket.websocketservice.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static com.websocket.websocketservice.mapper.WebsocketDtoMapperImpl.DATE_TIME_FORMATTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

  private final CurrencyRateRepository currencyRateRepository;
  private final WebsocketDtoMapper websocketDtoMapper;

  @Transactional
  public void upsertWebsocketCurrencyDto(WebsocketCurrencyDto websocketCurrencyDto) {

    if (currencyRateRepository.isCurrencyExistsByCode(websocketCurrencyDto.getCode())) {
      currencyRateRepository.updateCurrencyByCode(websocketCurrencyDto.getRate(),
          websocketCurrencyDto.getReverseRate(),
          LocalDate.parse(websocketCurrencyDto.getDate(), DATE_TIME_FORMATTER),
          websocketCurrencyDto.getCode());
      log.info("update operation performed in DB, code {}", websocketCurrencyDto.getCode());
      return;
    }

    CurrencyRateEntity currencyRateEntity =
        websocketDtoMapper.mapWebsocketDtoToCurrencyRateEntity(websocketCurrencyDto);

    Set<CurrencyRateNameEntity> currencyRateNameEntities =
        websocketDtoMapper.mapToCurrencyRateNames(websocketCurrencyDto.getNames());
    currencyRateEntity.addNames(currencyRateNameEntities);

    currencyRateRepository.save(currencyRateEntity);
    log.info("CurrencyRateEntity - [{}] created and created in DB", currencyRateEntity.toString());
  }
}
