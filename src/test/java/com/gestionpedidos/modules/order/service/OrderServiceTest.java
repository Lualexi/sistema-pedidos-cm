package com.gestionpedidos.modules.order.service;

import com.gestionpedidos.modules.order.model.Order;
import com.gestionpedidos.modules.order.model.OrderItem;
import com.gestionpedidos.modules.order.repository.OrderRepository;
import com.gestionpedidos.modules.order.service.impl.OrderServiceImpl;
import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Mouse Logitech")
                .price(new BigDecimal("25.00"))
                .stock(10)
                .build();

        // Mapeo corregido: usa .product() y .price()
        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(2)
                .price(new BigDecimal("25.00"))
                .build();

        order = Order.builder()
                .customerName("Carlos Perez")
                .items(List.of(item))
                .build();
    }

    @Test
    void createOrder_DeberiaCrearPedidoYDescontarStock_CuandoHayStockSuficiente() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(8, product.getStock());
        assertEquals(new BigDecimal("50.00"), createdOrder.getTotal());
        verify(productRepository, times(1)).save(product);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void createOrder_DeberiaLanzarExcepcion_CuandoStockEsInsuficiente() {
        product.setStock(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(order));

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateStatus_DeberiaReintegrarStock_CuandoPedidoEsCancelado() {
        order.setStatus("PENDIENTE");
        order.getItems().get(0).setProduct(product);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.updateStatus(1L, "CANCELADO");

        assertEquals(12, product.getStock());
        assertEquals("CANCELADO", order.getStatus());
        verify(productRepository, times(1)).save(product);
    }
}