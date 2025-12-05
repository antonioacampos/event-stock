package org.com.simplestockservice.service;

import org.com.simplestockservice.dto.StockEvent;
import org.com.simplestockservice.dto.StockErrorEvent;
import org.com.simplestockservice.model.Product;
import org.com.simplestockservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String AUDIT_TOPIC = "stock-audit-topic";
    private static final String ERROR_TOPIC = "stock-error-topic";

    @Transactional
    public Product updateStockQuantity(Long productId, int changeAmount) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int oldQuantity = product.getQuantity();
        int newQuantity = oldQuantity + changeAmount;

        if (newQuantity < 0) {
            StockErrorEvent errorEvent = new StockErrorEvent(
                    "INSUFFICIENT_STOCK",
                    "Estoque insuficiente para a operação. Qtd. atual: " + oldQuantity,
                    productId,
                    changeAmount,
                    oldQuantity,
                    product.getProductCode(),
                    LocalDateTime.now().toString()
            );
            kafkaTemplate.send(ERROR_TOPIC, product.getProductCode(), errorEvent);

            throw new RuntimeException("Estoque insuficiente para a operação.");
        }

        product.setQuantity(newQuantity);
        Product updatedProduct = repository.save(product);

        StockEvent event = new StockEvent(
                changeAmount > 0 ? "StockIncreased" : "StockDecreased",
                updatedProduct.getId(),
                oldQuantity,
                newQuantity,
                updatedProduct.getProductCode()
        );

        kafkaTemplate.send(AUDIT_TOPIC, updatedProduct.getProductCode(), event);

        return updatedProduct;
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Optional<Product> getProduct(Long id) {
        return repository.findById(id);
    }
}