package com.trade.service.trade_service.controller;

import com.trade.service.trade_service.model.Confirmation;
import com.trade.service.trade_service.service.ConfirmationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/confirmations")
public class ConfirmationController {

    private final ConfirmationService confirmationService;

    public ConfirmationController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Confirmation> generate(@RequestParam Long tradeId) {
        Confirmation c = confirmationService.generateForTrade(tradeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Confirmation> get(@PathVariable Long id) {
        return ResponseEntity.ok(confirmationService.getById(id));
    }

    @GetMapping("/by-trade/{tradeId}")
    public ResponseEntity<Confirmation> byTrade(@PathVariable Long tradeId) {
        return ResponseEntity.ok(confirmationService.getByTradeId(tradeId));
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<String> resend(@PathVariable Long id) {
        return ResponseEntity.ok(confirmationService.resend(id));
    }
}
