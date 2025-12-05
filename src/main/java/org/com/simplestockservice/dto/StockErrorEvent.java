package org.com.simplestockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockErrorEvent {
    private String errorCode; // Ex: "INSUFFICIENT_STOCK"
    private String errorMessage;
    private Long productId;
    private int requestedChange; // A quantidade que tentou mover (negativo se for saída)
    private int currentQuantity; // O estoque antes da tentativa de mudança
    private String productCode;
    private String timestamp = LocalDateTime.now().toString(); // Data/hora do erro
}
