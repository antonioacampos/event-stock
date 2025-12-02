package org.com.simplestockservice.controller;

import org.com.simplestockservice.model.Product;
import org.com.simplestockservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/product-stock")
public class StockController {

    @Autowired
    private StockService stockService;

    // Cria o produto inicial no banco de dados
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        return stockService.createProduct(product);
    }

    // Altera o estoque: positivo para entrada, negativo para saída
    @PutMapping("/{id}/{changeAmount}")
    public Product updateStock(@PathVariable Long id, @PathVariable int changeAmount) {
        try {
            return stockService.updateStockQuantity(id, changeAmount);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Consulta o estoque atual
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return stockService.getProduct(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
}