package com.trade.service.trade_service.controller;

import com.trade.service.trade_service.service.RateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReferenceController {

    private final RateService rateService;

    public ReferenceController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/ref/currencies")
    public ResponseEntity<List<String>> currencies() {
        return ResponseEntity.ok(rateService.supportedCurrencies());
    }

    @GetMapping("/ref/pairs")
    public ResponseEntity<List<String>> pairs() {
        return ResponseEntity.ok(rateService.supportedPairs());
    }

    @GetMapping("/rates/{pair}")
    public ResponseEntity<BigDecimal> getRate(@PathVariable String pair) {
        BigDecimal r = rateService.getRate(pair);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @PostMapping("/rates")
    public ResponseEntity<Void> setRate(@RequestParam String pair, @RequestParam BigDecimal rate) {
        rateService.setRate(pair, rate);
        return ResponseEntity.ok().build();
    }
}
