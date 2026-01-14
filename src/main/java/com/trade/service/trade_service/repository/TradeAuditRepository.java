package com.trade.service.trade_service.repository;

import com.trade.service.trade_service.model.TradeAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeAuditRepository extends JpaRepository<TradeAudit, Long> {
    List<TradeAudit> findByTradeIdOrderByTimestampAsc(Long tradeId);
}
