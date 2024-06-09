package com.websocket.websocketservice.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "currency_rate", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CurrencyRateEntity implements Persistable<Long>, Serializable {

  @Id
  @GeneratedValue(generator = "currency_rate_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "currency_rate_seq",
      sequenceName = "currency_rate_seq",
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

  @OneToMany(mappedBy = "entity",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private final Set<CurrencyRateNameEntity> names= new HashSet<>();

  @Column(name = "date")
  @ToString.Include
  private LocalDate date;

  @Version
  @ToString.Include
  private int version;

  @Override
  public boolean isNew() {
    return this.id == null;
  }

  public void addName(CurrencyRateNameEntity currencyRateNameEntity) {
    this.names.add(currencyRateNameEntity);
    currencyRateNameEntity.setEntity(this);
  }

  public void addNames(Set<CurrencyRateNameEntity> currencyNameEntities) {
    this.names.addAll(currencyNameEntities);
    currencyNameEntities.forEach(nameEntity -> nameEntity.setEntity(this));
  }

  public void removeName(CurrencyRateNameEntity currencyRateNameEntity) {
    this.names.remove(currencyRateNameEntity);
    currencyRateNameEntity.setEntity(this);
  }
}
