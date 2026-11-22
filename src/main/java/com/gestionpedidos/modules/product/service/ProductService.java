package com.gestionpedidos.modules.product.service;

import com.gestionpedidos.modules.product.model.InventoryMovement;
import com.gestionpedidos.modules.product.model.Product;

import java.util.List;

public interface ProductService {
    Product save(Product product);
    List<Product> findAll(String search);
    Product findById(Long id);
    Product update(Long id, Product productDetails);
    void deleteById(Long id);
    List<InventoryMovement> findMovements(Long productId);
}