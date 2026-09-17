package com.gestionpedidos.modules.product.repository;

import com.gestionpedidos.modules.product.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductInMemoryRepository {

    private final Map<Long, Product> productStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        productStorage.put(product.getId(), product);
        return product;
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productStorage.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productStorage.values());
    }

    public void deleteById(Long id) {
        productStorage.remove(id);
    }
}