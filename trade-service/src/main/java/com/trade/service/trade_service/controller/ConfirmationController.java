package com.trade.service.trade_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trade.service.trade_service.model.Confirmation;
import com.trade.service.trade_service.service.ConfirmationService;

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

    @GetMapping(path = "/{id}/pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        Confirmation c = confirmationService.getById(id);
        byte[] pdf = c.getPdfContent();
        if (pdf == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=confirmation-" + id + ".pdf")
                .body(pdf);
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
