package com.websocket.websocketservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.time.Instant;

@Entity
@Table(name = "currency_rate_name", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Builder(setterPrefix = "set", toBuilder = true)
public class CurrencyRateNameEntity implements Persistable<NamePK> {

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "currency_rate_id", foreignKey = @ForeignKey(name = "fk_name_currency_rate"), nullable = false)
  CurrencyRateEntity entity;

  @Id
  @Column(name = "language_id", nullable = false)
  private Integer language;

  @Column(name = "name", nullable = false)
  private String name;

  @Version
  @ToString.Include
  private int version;


  public NamePK getId() {
    return new NamePK(this.entity.getId(), this.getLanguage());
  }

  public boolean isNew() {
    return entity == null && language == null;
  }

  public Long getEntityId() {
    return this.entity != null ? this.entity.getId() : null;
  }

}
