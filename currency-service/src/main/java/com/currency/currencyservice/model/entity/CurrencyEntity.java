package com.currency.currencyservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "currency", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true, setterPrefix = "set")
public class CurrencyEntity implements Persistable<Long>, Serializable {

  @Id
  @GeneratedValue(generator = "currency_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "currency_seq",
      sequenceName = "currency_seq",
      schema = "public",
      allocationSize = 10, initialValue = 1)
  @ToString.Include
  private Long id;

  @ToString.Include
  private Integer ratio;

  @ToString.Include
  @Column(precision = 33, scale = 18)
  private BigDecimal rate;

  @ToString.Include
  @Column(precision = 33, scale = 18)
  private BigDecimal reverseRate;

  @Column(length = 3, unique = true)
  @ToString.Include
  private String code;

  @OneToMany(mappedBy = "currencyEntity",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private final Set<ChronologicalExchangeRateEntity> chronologicalExchangeRateEntities = new HashSet<>();

  @OneToMany(mappedBy = "entity",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private final Set<CurrencyNameEntity> names= new HashSet<>();

  @Column(name = "created_on")
  @ToString.Include
  private Instant createdOn;

  @Column(name = "updated_on")
  @ToString.Include
  private Instant updatedOn;

  @Column(name = "created_by")
  @ToString.Include
  private String createdBy;

  @Column(name = "date")
  @ToString.Include
  private LocalDate date;

  @ToString.Include
  @Column(length = 3)
  private String source;

  @Version
  @ToString.Include
  private int version;

  @Override
  public boolean isNew() {
    return this.id == null;
  }

  public void addChronologicalExchangeRateEntity(ChronologicalExchangeRateEntity exchangeRateEntity) {
    this.chronologicalExchangeRateEntities.add(exchangeRateEntity);
    exchangeRateEntity.setCurrencyEntity(this);
  }

  public void removeChronologicalExchangeRateEntity(ChronologicalExchangeRateEntity exchangeRateEntity) {
    this.chronologicalExchangeRateEntities.remove(exchangeRateEntity);
    exchangeRateEntity.setCurrencyEntity(null);
  }

  public void addName(CurrencyNameEntity currencyNameEntity) {
    this.names.add(currencyNameEntity);
    currencyNameEntity.setEntity(this);
  }

  public void addNames(List<CurrencyNameEntity> currencyNameEntities) {
    this.names.addAll(currencyNameEntities);
    currencyNameEntities.forEach(nameEntity -> nameEntity.setEntity(this));
  }

  public void removeName(CurrencyNameEntity currencyNameEntity) {
    this.names.remove(currencyNameEntity);
    currencyNameEntity.setEntity(this);
  }
}
