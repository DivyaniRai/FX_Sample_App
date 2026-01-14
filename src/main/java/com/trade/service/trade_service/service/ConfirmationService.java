package com.trade.service.trade_service.service;

import com.trade.service.trade_service.model.Confirmation;

public interface ConfirmationService {
    Confirmation generateForTrade(Long tradeId);
    Confirmation getById(Long id);
    Confirmation getByTradeId(Long tradeId);
    String resend(Long id);
}
