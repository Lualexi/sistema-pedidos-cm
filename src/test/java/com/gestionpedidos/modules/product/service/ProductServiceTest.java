package com.gestionpedidos.modules.product.service;

import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.ProductRepository;
import com.gestionpedidos.modules.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Laptop Lenovo")
                .description("Core i7 16GB")
                .price(new BigDecimal("1200.00"))
                .stock(10)
                .build();
    }

    @Test
    void save_DeberiaGuardarProducto_CuandoDatosSeanValidos() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        assertNotNull(savedProduct);
        assertEquals("Laptop Lenovo", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void findById_DeberiaRetornarProducto_CuandoExisteId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
    }

    @Test
    void findById_DeberiaLanzarExcepcion_CuandoNoExisteId() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> productService.findById(99L));

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void save_DeberiaLanzarExcepcion_CuandoPrecioEsNegativo() {
        product.setPrice(new BigDecimal("-50.00"));

        assertThrows(RuntimeException.class, () -> productService.save(product));
        verify(productRepository, never()).save(any());
    }
}