package com.trade.service.trade_service.service.impl;

import com.trade.service.trade_service.dto.TradeRequest;
import com.trade.service.trade_service.model.Trade;
import com.trade.service.trade_service.model.TradeAudit;
import com.trade.service.trade_service.model.enums.TradeStatus;
import com.trade.service.trade_service.model.enums.TradeType;
import com.trade.service.trade_service.repository.TradeAuditRepository;
import com.trade.service.trade_service.repository.TradeRepository;
import com.trade.service.trade_service.service.TradeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final TradeAuditRepository auditRepository;

    public TradeServiceImpl(TradeRepository tradeRepository, TradeAuditRepository auditRepository) {
        this.tradeRepository = tradeRepository;
        this.auditRepository = auditRepository;
    }

    private void addAudit(Long tradeId, String status, String user) {
        TradeAudit a = new TradeAudit();
        a.setTradeId(tradeId);
        a.setStatus(status);
        a.setTimestamp(OffsetDateTime.now());
        a.setUserOrSystem(user);
        auditRepository.save(a);
    }

    @Override
    public Trade createSpot(TradeRequest req) {
        Trade t = new Trade();
        t.setTradeType(TradeType.SPOT);
        t.setBuyCurrency(req.getBuyCurrency());
        t.setSellCurrency(req.getSellCurrency());
        t.setRate(req.getRate());
        t.setCustomerId(req.getCustomerId());
        t.setTradeDate(req.getTradeDate() == null ? LocalDate.now() : req.getTradeDate());
        t.setValueDate(t.getTradeDate().plusDays(2));
        if (req.getBuyAmount() != null) {
            t.setBuyAmount(req.getBuyAmount());
            t.setSellAmount(req.getBuyAmount().divide(req.getRate(), 4, BigDecimal.ROUND_HALF_UP));
        } else if (req.getSellAmount() != null) {
            t.setSellAmount(req.getSellAmount());
            t.setBuyAmount(req.getSellAmount().multiply(req.getRate()));
        }
        t.setStatus(TradeStatus.NEW);
        Trade saved = tradeRepository.save(t);
        addAudit(saved.getId(), saved.getStatus().name(), "SYSTEM");
        return saved;
    }

    @Override
    public Trade createForward(TradeRequest req) {
        Trade t = new Trade();
        t.setTradeType(TradeType.FORWARD);
        t.setBuyCurrency(req.getBuyCurrency());
        t.setSellCurrency(req.getSellCurrency());
        t.setRate(req.getRate());
        t.setCustomerId(req.getCustomerId());
        t.setTradeDate(req.getTradeDate() == null ? LocalDate.now() : req.getTradeDate());
        if (req.getValueDate() != null) {
            t.setValueDate(req.getValueDate());
        } else if (req.getTenor() != null) {
            // simple tenor parsing: number + M
            String s = req.getTenor().toUpperCase();
            if (s.endsWith("M")) {
                int months = Integer.parseInt(s.substring(0, s.length()-1));
                t.setValueDate(t.getTradeDate().plusMonths(months));
            } else {
                t.setValueDate(t.getTradeDate().plusDays(30));
            }
        } else {
            t.setValueDate(t.getTradeDate().plusDays(30));
        }
        if (req.getBuyAmount() != null) {
            t.setBuyAmount(req.getBuyAmount());
            t.setSellAmount(req.getBuyAmount().divide(req.getRate(), 4, BigDecimal.ROUND_HALF_UP));
        } else if (req.getSellAmount() != null) {
            t.setSellAmount(req.getSellAmount());
            t.setBuyAmount(req.getSellAmount().multiply(req.getRate()));
        }
        t.setStatus(TradeStatus.NEW);
        Trade saved = tradeRepository.save(t);
        addAudit(saved.getId(), saved.getStatus().name(), "SYSTEM");
        return saved;
    }

    @Override
    public Optional<Trade> getById(Long id) {
        return tradeRepository.findById(id);
    }

    @Override
    public List<Trade> list(String customerId, TradeType type, TradeStatus status, LocalDate from, LocalDate to) {
        return tradeRepository.filter(customerId, type, status, from, to);
    }

    @Override
    public Trade update(Long id, TradeRequest req) {
        Trade t = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trade not found"));
        if (t.getStatus() != TradeStatus.NEW) throw new IllegalStateException("Only NEW trades can be edited");
        if (req.getBuyAmount() != null) {
            t.setBuyAmount(req.getBuyAmount());
            t.setSellAmount(req.getBuyAmount().divide(req.getRate(),4, BigDecimal.ROUND_HALF_UP));
        }
        if (req.getSellAmount() != null) {
            t.setSellAmount(req.getSellAmount());
            t.setBuyAmount(req.getSellAmount().multiply(req.getRate()));
        }
        if (req.getRate() != null) t.setRate(req.getRate());
        tradeRepository.save(t);
        addAudit(t.getId(), "UPDATED", "USER");
        return t;
    }

    @Override
    public Trade confirm(Long id, String user) {
        Trade t = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trade not found"));
        if (t.getStatus() != TradeStatus.NEW) throw new IllegalStateException("Only NEW trades can be confirmed");
        t.setStatus(TradeStatus.CONFIRMED);
        tradeRepository.save(t);
        addAudit(t.getId(), t.getStatus().name(), user);
        return t;
    }

    @Override
    public Trade cancel(Long id, String user) {
        Trade t = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trade not found"));
        if (t.getStatus() == TradeStatus.CANCELLED) throw new IllegalStateException("Already cancelled");
        t.setStatus(TradeStatus.CANCELLED);
        tradeRepository.save(t);
        addAudit(t.getId(), t.getStatus().name(), user);
        return t;
    }
}
