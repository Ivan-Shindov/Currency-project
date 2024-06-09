package com.currency.currencyservice.service.connector;

import com.currency.currencyservice.enumerator.Language;
import com.currency.currencyservice.model.dto.CurrencyNameDtoBg;
import com.currency.currencyservice.model.dto.CurrencyRateBgWrapperDto;
import com.currency.currencyservice.model.dto.CurrencyRateBnb;
import com.currency.currencyservice.model.dto.CurrencyRateDto;
import com.currency.currencyservice.model.dto.CurrencyRateWrapperDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BnbConnector {

  private final XmlMapper bnbXmlMapper;

  @Value("${currency.currencyExchange.bnbURLen}")
  private String bnbUrlEn;
  @Value("${currency.currencyExchange.bnbURLbg}")
  private String bnbUrlBg;

  private String getURLContent(String bnbUrl) {
    StringBuilder sbResponse = new StringBuilder();
    URL url;
    try {
      url = new URL(bnbUrl);
      URLConnection connection = url.openConnection();

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String sLine;
        while ((sLine = reader.readLine()) != null) {
          sbResponse.append(sLine);
        }
      }
    } catch (MalformedURLException ex) {
      log.error("Bad URL: {}", bnbUrl, ex);
    } catch (IOException ex) {
      log.error("IOException while trying to read from URL: {}", bnbUrl, ex);
    }
    return sbResponse.toString();
  }

  public List<CurrencyRateBnb> getCurrencyRates() {
    List<CurrencyRateBnb> currencyRates = new ArrayList<>();
    try {
      String bnbContentEn = getURLContent(bnbUrlEn);
      log.debug("Got the following response from BNB: {}", bnbContentEn);
      CurrencyRateWrapperDto currencyRateEnWrapperDto = getCurrencyRateEnWrapperDto(bnbContentEn);

      String bnbContentBg = getURLContent(bnbUrlBg);
      log.debug("Got the following response from BNB: {}", bnbContentBg);
      CurrencyRateBgWrapperDto currencyRateBgWrapperDto = getCurrencyRateBgWrapperDto(bnbContentBg);

      addToCurrencyRatesBnb(currencyRates, currencyRateEnWrapperDto, currencyRateBgWrapperDto);
    } catch (JsonProcessingException ex) {
      log.error("Exception while deserializing currency rates.", ex);
      throw new RuntimeException(ex);
    }
    return currencyRates;
  }

  private CurrencyRateWrapperDto getCurrencyRateEnWrapperDto(String bnbContentEn)
      throws JsonProcessingException {
    CurrencyRateWrapperDto currencyRateEnWrapperDto = bnbXmlMapper.readValue(
        bnbContentEn, CurrencyRateWrapperDto.class);
    currencyRateEnWrapperDto.getCurrencyRates().remove(0);
    return currencyRateEnWrapperDto;
  }

  private CurrencyRateBgWrapperDto getCurrencyRateBgWrapperDto(String bnbContentBg)
      throws JsonProcessingException {
    CurrencyRateBgWrapperDto currencyRateBgWrapperDto =
        bnbXmlMapper.readValue(bnbContentBg, CurrencyRateBgWrapperDto.class);
    currencyRateBgWrapperDto.getCurrencyBgNames().remove(0);
    return currencyRateBgWrapperDto;
  }

  private void addToCurrencyRatesBnb(List<CurrencyRateBnb> currencyRates,
      CurrencyRateWrapperDto currencyRateEnWrapperDto, CurrencyRateBgWrapperDto currencyRateBgWrapperDto) {

    for (int i = 0; i < currencyRateEnWrapperDto.getCurrencyRates().size(); i++) {
      CurrencyRateDto currencyRateDto = currencyRateEnWrapperDto.getCurrencyRates().get(i);
      CurrencyNameDtoBg currencyNameDtoBg = currencyRateBgWrapperDto.getCurrencyBgNames().get(i);
      CurrencyRateBnb currencyRateBnb = buildCurrencyRateBnb(currencyRateDto, currencyNameDtoBg);
      currencyRates.add(currencyRateBnb);
    }
  }

  private CurrencyRateBnb buildCurrencyRateBnb(CurrencyRateDto currencyRateDto,
      CurrencyNameDtoBg currencyNameDtoBg) {

    CurrencyRateBnb currencyRateBnb = CurrencyRateBnb.builder()
        .setCode(currencyRateDto.getCode())
        .setRate(currencyRateDto.getRate())
        .setGold(currencyRateDto.getGold())
        .setDate(currencyRateDto.getDate())
        .setRatio(currencyRateDto.getRatio())
        .setReverseRate(currencyRateDto.getReverseRate())
        .build();

    currencyRateBnb.getNames().put(Language.ENGLISH.getId(), currencyRateDto.getName());
    currencyRateBnb.getNames().put(Language.BULGARIAN.getId(), currencyNameDtoBg.getBgName());

    return currencyRateBnb;
  }
}
