package com.currency.currencyservice.enumerator;

import lombok.Getter;

@Getter
public enum Language {

  ENGLISH(1, "EN"),
  BULGARIAN(2, "BG");

  private Integer id;
  private String name;


  Language(int id, String name) {
    this.id = id;
    this.name = name;
  }
}
