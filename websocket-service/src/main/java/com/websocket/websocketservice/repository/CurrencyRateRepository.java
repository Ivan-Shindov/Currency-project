package com.websocket.websocketservice.repository;

import com.websocket.websocketservice.model.CurrencyRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRateEntity, Long> {

  @Query(value = """
          SELECT EXISTS(
              SELECT NULL 
              FROM public.currency_rate cr
              WHERE cr.code = :code)
          """, nativeQuery = true)
  boolean isCurrencyExistsByCode(@Param("code") String currencyRateCode);

  @Modifying
  @Query(value = """
          UPDATE public.currency_rate
          SET rate = :rate,
              reverse_rate = :reverseRate,
              date = :date,
              "version" = "version" + 1
          WHERE code = :code
          """, nativeQuery = true)
  Integer updateCurrencyByCode(@Param("rate") BigDecimal rate,
      @Param("reverseRate") BigDecimal reverseRate,
      @Param("date") LocalDate date, @Param("code") String code);
}
