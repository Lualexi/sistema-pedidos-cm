package com.gestionpedidos.modules.product.service.impl;

import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.ProductRepository;
import com.gestionpedidos.modules.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        validateProductData(product);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    @Override
    @Transactional
    public Product update(Long id, Product productDetails) {
        validateProductData(productDetails);
        Product existingProduct = findById(id);

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStock(productDetails.getStock());

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private void validateProductData(Product product) {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio del producto debe ser mayor o igual a 0.");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new RuntimeException("El stock del producto no puede ser negativo.");
        }
    }
}