package org.com.simplestockservice.service;

import org.com.simplestockservice.dto.StockErrorEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockErrorListener {

    @KafkaListener(topics = "stock-error-topic", groupId = "error-event-group")
    public void handleErrorEvent(StockErrorEvent event) {
        System.err.println("!!! ERRO DE ESTOQUE - ALERTA ASYNC !!!");
        System.err.println("Mensagem: " + event.getErrorMessage());
        System.err.println("Produto: " + event.getProductCode() + " (ID: " + event.getProductId() + ")");
        System.err.println("Qtd. Anterior: " + event.getCurrentQuantity() + ", Mudança Solicitada: " + event.getRequestedChange());
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}