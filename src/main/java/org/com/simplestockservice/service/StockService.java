package org.com.simplestockservice.service;

import org.com.simplestockservice.dto.StockEvent;
import org.com.simplestockservice.model.Product;
import org.com.simplestockservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "stock-audit-topic";

    @Transactional
    public Product updateStockQuantity(Long productId, int changeAmount) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int oldQuantity = product.getQuantity();
        int newQuantity = oldQuantity + changeAmount;

        if (newQuantity < 0) {
            throw new RuntimeException("Estoque insuficiente para a operação.");
        }

        product.setQuantity(newQuantity);
        Product updatedProduct = repository.save(product); // 1. Persiste no MySQL

        // 2. Publica o Evento no Kafka para auditoria/notificação
        StockEvent event = new StockEvent(
                changeAmount > 0 ? "StockIncreased" : "StockDecreased",
                updatedProduct.getId(),
                oldQuantity,
                newQuantity,
                updatedProduct.getProductCode()
        );

        // A chave (key) garante que todos os eventos do mesmo produto vão para a mesma partição, se houver
        kafkaTemplate.send(TOPIC, updatedProduct.getProductCode(), event);

        return updatedProduct;
    }

    // Métodos auxiliares
    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Optional<Product> getProduct(Long id) {
        return repository.findById(id);
    }
}