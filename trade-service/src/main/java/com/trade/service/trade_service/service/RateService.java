package com.trade.service.trade_service.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RateService {
    private final Map<String, BigDecimal> rates = Collections.synchronizedMap(new HashMap<>());

    public RateService() {
        rates.put("EURUSD", new BigDecimal("1.10"));
        rates.put("USDINR", new BigDecimal("83.50"));
        rates.put("GBPUSD", new BigDecimal("1.28"));
    }

    public BigDecimal getRate(String pair) {
        return rates.get(pair.toUpperCase());
    }

    public void setRate(String pair, BigDecimal rate) {
        rates.put(pair.toUpperCase(), rate);
    }

    public List<String> supportedCurrencies() {
        return List.of("USD","EUR","GBP","JPY","INR");
    }

    public List<String> supportedPairs() {
        return rates.keySet().stream().toList();
    }
}
