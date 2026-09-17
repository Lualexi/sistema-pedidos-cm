package com.gestionpedidos.modules.product.service.impl;

import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.ProductInMemoryRepository;
import com.gestionpedidos.modules.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductInMemoryRepository productRepository;

    public ProductServiceImpl(ProductInMemoryRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}