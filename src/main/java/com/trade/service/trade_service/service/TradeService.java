package com.trade.service.trade_service.service;

import com.trade.service.trade_service.dto.TradeRequest;
import com.trade.service.trade_service.model.Trade;
import com.trade.service.trade_service.model.enums.TradeStatus;
import com.trade.service.trade_service.model.enums.TradeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TradeService {
    Trade createSpot(TradeRequest req);
    Trade createForward(TradeRequest req);
    Optional<Trade> getById(Long id);
    List<Trade> list(String customerId, TradeType type, TradeStatus status, LocalDate from, LocalDate to);
    Trade update(Long id, TradeRequest req);
    Trade confirm(Long id, String user);
    Trade cancel(Long id, String user);
}
