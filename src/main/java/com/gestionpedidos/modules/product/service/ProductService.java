package com.gestionpedidos.modules.product.service;

import com.gestionpedidos.modules.product.model.Product;
import java.util.List;

public interface ProductService {
    Product save(Product product);
    List<Product> findAll();
    Product findById(Long id);
    Product update(Long id, Product product);
    void deleteById(Long id);
}