package com.trade.service.trade_service.repository;

import com.trade.service.trade_service.model.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Optional<Confirmation> findByTradeId(Long tradeId);
}
