package com.trade.service.trade_service.model;

import com.trade.service.trade_service.model.enums.TradeStatus;
import com.trade.service.trade_service.model.enums.TradeType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    private String buyCurrency;
    private String sellCurrency;

    private BigDecimal buyAmount;
    private BigDecimal sellAmount;

    private BigDecimal rate;

    private LocalDate tradeDate;
    private LocalDate valueDate;

    private String customerId;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(String buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    public String getSellCurrency() {
        return sellCurrency;
    }

    public void setSellCurrency(String sellCurrency) {
        this.sellCurrency = sellCurrency;
    }

    public java.math.BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(java.math.BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public java.math.BigDecimal getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(java.math.BigDecimal sellAmount) {
        this.sellAmount = sellAmount;
    }

    public java.math.BigDecimal getRate() {
        return rate;
    }

    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }
}
