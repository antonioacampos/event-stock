package org.com.simplestockservice.service;

import org.com.simplestockservice.dto.StockEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockAuditListener {

    @KafkaListener(topics = "stock-audit-topic", groupId = "audit-event-group")
    public void handleStockEvent(StockEvent event) {
        System.out.println("--- LOG/AUDITORIA ASYNC ---");
        System.out.println("Evento Recebido: " + event.getEventType());
        System.out.println("Produto: " + event.getProductCode() + " (ID: " + event.getProductId() + ")");
        System.out.println("Mudança de Estoque: " + event.getOldQuantity() + " -> " + event.getNewQuantity());
        System.out.println("---------------------------");
    }
}