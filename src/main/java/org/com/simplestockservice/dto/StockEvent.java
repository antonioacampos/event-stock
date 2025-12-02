package org.com.simplestockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockEvent {
    private String eventType; // Ex: "StockIncreased", "StockDecreased"
    private Long productId;
    private int oldQuantity;
    private int newQuantity;
    private String productCode;
}