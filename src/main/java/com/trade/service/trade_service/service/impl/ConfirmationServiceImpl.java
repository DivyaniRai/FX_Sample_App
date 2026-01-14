package com.trade.service.trade_service.service.impl;

import com.trade.service.trade_service.model.Confirmation;
import com.trade.service.trade_service.model.Trade;
import com.trade.service.trade_service.repository.ConfirmationRepository;
import com.trade.service.trade_service.repository.TradeRepository;
import com.trade.service.trade_service.service.ConfirmationService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationRepository confirmationRepository;
    private final TradeRepository tradeRepository;

    public ConfirmationServiceImpl(ConfirmationRepository confirmationRepository, TradeRepository tradeRepository) {
        this.confirmationRepository = confirmationRepository;
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Confirmation generateForTrade(Long tradeId) {
        Trade t = tradeRepository.findById(tradeId).orElseThrow(() -> new NoSuchElementException("Trade not found"));
        Confirmation c = new Confirmation();
        c.setTradeId(tradeId);
        String text = String.format("Confirmation for trade %d: %s %s/%s buy %s sell %s rate %s valueDate %s", t.getId(), t.getTradeType(), t.getBuyCurrency(), t.getSellCurrency(), t.getBuyAmount(), t.getSellAmount(), t.getRate(), t.getValueDate());
        c.setConfirmationText(text);
        c.setCreatedAt(OffsetDateTime.now());
        return confirmationRepository.save(c);
    }

    @Override
    public Confirmation getById(Long id) {
        return confirmationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Confirmation not found"));
    }

    @Override
    public Confirmation getByTradeId(Long tradeId) {
        return confirmationRepository.findByTradeId(tradeId).orElseThrow(() -> new NoSuchElementException("Confirmation not found for trade"));
    }

    @Override
    public String resend(Long id) {
        Confirmation c = getById(id);
        // simulate resend
        return "Resent confirmation " + c.getId();
    }
}
