package com.trade.service.trade_service.repository;

import com.trade.service.trade_service.model.Trade;
import com.trade.service.trade_service.model.enums.TradeStatus;
import com.trade.service.trade_service.model.enums.TradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByCustomerId(String customerId);
    List<Trade> findByTradeType(TradeType tradeType);
    List<Trade> findByStatus(TradeStatus status);

    @Query("select t from Trade t where (:customerId is null or t.customerId = :customerId) and (:type is null or t.tradeType = :type) and (:status is null or t.status = :status) and (:from is null or t.tradeDate >= :from) and (:to is null or t.tradeDate <= :to)")
    List<Trade> filter(@Param("customerId") String customerId,
                       @Param("type") TradeType type,
                       @Param("status") TradeStatus status,
                       @Param("from") LocalDate from,
                       @Param("to") LocalDate to);
}
