package com.trade.service.trade_service.controller;

import com.trade.service.trade_service.dto.TradeRequest;
import com.trade.service.trade_service.model.Trade;
import com.trade.service.trade_service.model.TradeAudit;
import com.trade.service.trade_service.model.enums.TradeStatus;
import com.trade.service.trade_service.model.enums.TradeType;
import com.trade.service.trade_service.repository.TradeAuditRepository;
import com.trade.service.trade_service.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;
    private final TradeAuditRepository auditRepository;

    public TradeController(TradeService tradeService, TradeAuditRepository auditRepository) {
        this.tradeService = tradeService;
        this.auditRepository = auditRepository;
    }

    @PostMapping("/spot")
    public ResponseEntity<Trade> createSpot(@Valid @RequestBody TradeRequest req) {
        Trade t = tradeService.createSpot(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @PostMapping("/forward")
    public ResponseEntity<Trade> createForward(@Valid @RequestBody TradeRequest req) {
        Trade t = tradeService.createForward(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trade> getById(@PathVariable Long id) {
        return tradeService.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Trade>> list(@RequestParam(required = false) String customerId,
                                            @RequestParam(required = false) TradeType tradeType,
                                            @RequestParam(required = false) TradeStatus status,
                                            @RequestParam(required = false) LocalDate tradeDateFrom,
                                            @RequestParam(required = false) LocalDate tradeDateTo) {
        List<Trade> list = tradeService.list(customerId, tradeType, status, tradeDateFrom, tradeDateTo);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trade> update(@PathVariable Long id, @Valid @RequestBody TradeRequest req) {
        Trade t = tradeService.update(id, req);
        return ResponseEntity.ok(t);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Trade> confirm(@PathVariable Long id, @RequestHeader(value = "X-USER", defaultValue = "qa") String user) {
        Trade t = tradeService.confirm(id, user);
        return ResponseEntity.ok(t);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Trade> cancel(@PathVariable Long id, @RequestHeader(value = "X-USER", defaultValue = "qa") String user) {
        Trade t = tradeService.cancel(id, user);
        return ResponseEntity.ok(t);
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<List<TradeAudit>> audit(@PathVariable Long id) {
        List<TradeAudit> audits = auditRepository.findByTradeIdOrderByTimestampAsc(id);
        return ResponseEntity.ok(audits);
    }
}
