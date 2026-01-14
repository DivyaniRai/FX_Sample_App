package com.trade.service.trade_service.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.trade.service.trade_service.model.Confirmation;
import com.trade.service.trade_service.model.Trade;
import com.trade.service.trade_service.repository.ConfirmationRepository;
import com.trade.service.trade_service.repository.TradeRepository;
import com.trade.service.trade_service.service.ConfirmationService;

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
        String text = String.format("Confirmation for trade %d: %s %s/%s buy %s sell %s rate %s valueDate %s",
                t.getId(), t.getTradeType(), t.getBuyCurrency(), t.getSellCurrency(), t.getBuyAmount(), t.getSellAmount(), t.getRate(), t.getValueDate());
        c.setConfirmationText(text);
        c.setCreatedAt(OffsetDateTime.now());
        Confirmation saved = confirmationRepository.save(c);

        // generate PDF containing the confirmation text
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(50, 750);
                cs.showText("Trade Confirmation");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(50, 720);
                // simple wrap: write whole text (acceptable for small strings)
                cs.showText(text);
                cs.endText();
            }
            doc.save(baos);
            saved.setPdfContent(baos.toByteArray());
            confirmationRepository.save(saved);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }

        return saved;
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
