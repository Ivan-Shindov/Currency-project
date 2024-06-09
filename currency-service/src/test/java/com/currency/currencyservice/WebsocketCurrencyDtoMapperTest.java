package com.currency.currencyservice;

import com.currency.currencyservice.mapper.WebsocketCurrencyDtoMapper;
import com.currency.currencyservice.mapper.WebsocketCurrencyDtoMapperImpl;
import com.currency.currencyservice.model.dto.WebsocketCurrencyDto;
import com.currency.currencyservice.model.entity.CurrencyEntity;
import com.currency.currencyservice.model.entity.CurrencyNameEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class WebsocketCurrencyDtoMapperTest {

  private WebsocketCurrencyDtoMapper websocketCurrencyDtoMapper;

  @BeforeEach
  public void setUp() {
    websocketCurrencyDtoMapper = new WebsocketCurrencyDtoMapperImpl();
  }

  @Test
  public void testMapEntityToWebsocketDto() {
    CurrencyEntity currencyEntity = buildCurrencyEntityTest1();
    WebsocketCurrencyDto expectedWebsocketCurrencyDto = buildExpectedCurrencyDtoTest1();

    WebsocketCurrencyDto websocketCurrencyDto =
        websocketCurrencyDtoMapper.mapEntityToWebsocketDto(currencyEntity);

    assertThat(websocketCurrencyDto).usingRecursiveComparison()
        .isEqualTo(expectedWebsocketCurrencyDto);
  }

  private WebsocketCurrencyDto buildExpectedCurrencyDtoTest1() {
    return WebsocketCurrencyDto.builder()
        .setRatio(100)
        .setReverseRate(BigDecimal.valueOf(0.81))
        .setRate(BigDecimal.valueOf(1.23))
        .setCode("USD")
        .setNames(Map.of(
            1, "Dollar",
            2, "Американски долар"
        ))
        .setDate("2024-10-15")
        .build();
  }

  private CurrencyEntity buildCurrencyEntityTest1() {

    CurrencyEntity currencyEntity = new CurrencyEntity();
    currencyEntity.setCode("USD");
    currencyEntity.setRate(BigDecimal.valueOf(1.23));
    currencyEntity.setReverseRate(BigDecimal.valueOf(0.81));
    currencyEntity.setRatio(100);
    currencyEntity.setDate(LocalDate.of(2024, 10, 15));

    Set<CurrencyNameEntity> names = new HashSet<>();
    CurrencyNameEntity currencyNameEntity = new CurrencyNameEntity();
    currencyNameEntity.setName("Dollar");
    currencyNameEntity.setLanguage(1);
    names.add(currencyNameEntity);

    CurrencyNameEntity currencyNameEntity2 = new CurrencyNameEntity();
    currencyNameEntity2.setName("Американски долар");
    currencyNameEntity2.setLanguage(2);
    names.add(currencyNameEntity2);

    currencyEntity.getNames().addAll(names);
    return currencyEntity;
  }
}
