package com.currency.currencyservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "chronological_exchange_rate", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Builder(setterPrefix = "set", toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChronologicalExchangeRateEntity implements Persistable<Long>, Serializable {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "chronological_exchange_rate_gen")
  @SequenceGenerator(
      name = "chronological_exchange_rate_gen",
      sequenceName = "chronological_exchange_rate_seq",
      schema = "public", allocationSize = 10, initialValue = 1)
  @ToString.Include
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "created_on")
  @ToString.Include
  private Instant createdOn;

  @ToString.Include
  @EqualsAndHashCode.Include
  @Column(precision = 33, scale = 18)
  private BigDecimal rate;

  @ToString.Include
  @EqualsAndHashCode.Include
  @Column(precision = 33, scale = 18)
  private BigDecimal reverseRate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "currency_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_chronological_exchange_rate_currency"))
  private CurrencyEntity currencyEntity;

  @Override
  public boolean isNew() {
    return id == null;
  }
}
