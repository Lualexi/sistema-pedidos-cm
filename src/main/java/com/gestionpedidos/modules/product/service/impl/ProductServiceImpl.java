package com.gestionpedidos.modules.product.service.impl;

import com.gestionpedidos.modules.product.model.InventoryMovement;
import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.InventoryMovementRepository;
import com.gestionpedidos.modules.product.repository.ProductRepository;
import com.gestionpedidos.modules.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;

    public ProductServiceImpl(ProductRepository productRepository, InventoryMovementRepository movementRepository) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        validateProductData(product);
        applyDefaults(product);
        Product saved = productRepository.save(product);

        if (saved.getStock() > 0) {
            movementRepository.save(InventoryMovement.builder()
                    .product(saved)
                    .movementType("ENTRADA")
                    .quantity(saved.getStock())
                    .stockAfter(saved.getStock())
                    .reference("Alta de producto #" + saved.getId())
                    .createdAt(LocalDateTime.now())
                    .build());
        }
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll(String search) {
        if (search != null && !search.isBlank()) {
            String q = search.trim();
            return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
        }
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
        applyDefaults(productDetails);
        Product existingProduct = findById(id);

        int previousStock = existingProduct.getStock();
        int newStock = productDetails.getStock();

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStock(newStock);
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setCurrency(productDetails.getCurrency());
        existingProduct.setMinStock(productDetails.getMinStock());
        existingProduct.setUnitWeightKg(productDetails.getUnitWeightKg());
        existingProduct.setUnitVolumeM3(productDetails.getUnitVolumeM3());

        Product updated = productRepository.save(existingProduct);

        if (newStock != previousStock) {
            int diff = Math.abs(newStock - previousStock);
            String type = newStock > previousStock ? "ENTRADA" : "AJUSTE";
            movementRepository.save(InventoryMovement.builder()
                    .product(updated)
                    .movementType(type)
                    .quantity(diff)
                    .stockAfter(newStock)
                    .reference("Ajuste manual producto #" + updated.getId())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        return updated;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> findMovements(Long productId) {
        if (productId != null) {
            return movementRepository.findByProductIdOrderByCreatedAtDesc(productId);
        }
        return movementRepository.findTop100ByOrderByCreatedAtDesc();
    }

    private void validateProductData(Product product) {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio del producto debe ser mayor o igual a 0.");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new RuntimeException("El stock del producto no puede ser negativo.");
        }
    }

    private void applyDefaults(Product product) {
        if (product.getCurrency() == null || product.getCurrency().isBlank()) {
            product.setCurrency("PEN");
        }
        if (product.getMinStock() == null || product.getMinStock() < 0) {
            product.setMinStock(10);
        }
    }
}