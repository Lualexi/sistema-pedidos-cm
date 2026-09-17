package com.gestionpedidos.modules.product.service;

import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.ProductInMemoryRepository;
import com.gestionpedidos.modules.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;
    private ProductInMemoryRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductInMemoryRepository();
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    @DisplayName("Debe registrar un producto correctamente en el catálogo")
    void shouldCreateProductSuccessfully() {
        // Corrección: Solo 5 argumentos según Product.java (id, name, description, price, stock)
        Product product = new Product(null, "Arroz 1kg", "Abarrotes", 4.50, 100);
        
        Product created = productService.save(product);

        assertNotNull(created.getId());
        assertEquals("Arroz 1kg", created.getName());
        assertEquals(100, created.getStock());
    }

    @Test
    @DisplayName("Debe listar los productos almacenados")
    void shouldReturnAllProducts() {
        // Corrección: Solo 5 argumentos
        productRepository.save(new Product(null, "Leche", "Lácteos", 3.80, 50));
        
        var products = productService.findAll();

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }
}