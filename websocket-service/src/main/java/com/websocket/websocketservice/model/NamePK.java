package com.websocket.websocketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(setterPrefix = "set")
public class NamePK implements Serializable {

  private Long entity;

  private Integer language;

  public static NamePK createPrimaryKey(Long id, Integer languageId) {
    return new NamePK(id, languageId);
  }

}
