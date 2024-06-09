package com.currency.currencyservice.model.entity;

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
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.time.Instant;

@Entity
@Table(name = "currency_name", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Builder(setterPrefix = "set", toBuilder = true)
public class CurrencyNameEntity  implements Persistable<NamePK> {

  @Id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "currency_id", foreignKey = @ForeignKey(name = "fk_name_currency"), nullable = false)
  CurrencyEntity entity;

  @Id
  @Column(name = "language_id", nullable = false)
  private Integer language;

  @Column(name = "name", nullable = false)
  private String name;

  @ToString.Include
  private Instant createdOn;

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
