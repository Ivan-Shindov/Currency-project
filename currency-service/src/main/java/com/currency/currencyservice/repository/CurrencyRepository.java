package com.currency.currencyservice.repository;

import com.currency.currencyservice.model.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

  @Query(value = """
          SELECT EXISTS(
              SELECT NULL 
              FROM public.currency c
              WHERE c.code = :code)
          """, nativeQuery = true)
  boolean isCurrencyExistsByCode(@Param("code") String currencyRateCode);

  @Query("""
      SELECT cr
      FROM CurrencyEntity cr
      JOIN FETCH cr.names
      WHERE cr.code IN (:codes)
      """)
  List<CurrencyEntity> getCurrenciesByCodes(@Param("codes") List<String> codes);
}
