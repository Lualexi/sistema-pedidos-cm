package com.gestionpedidos.modules.order.service;

import com.gestionpedidos.modules.order.model.Order;
import com.gestionpedidos.modules.order.repository.OrderInMemoryRepository;
import com.gestionpedidos.modules.order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;
    private OrderInMemoryRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderInMemoryRepository();
        // Corrección: Pasa únicamente OrderInMemoryRepository
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    @DisplayName("Debe crear una orden de pedido correctamente")
    void shouldCreateOrderSuccessfully() {
        Order newOrder = new Order();
        
        
        Order createdOrder = orderService.createOrder(newOrder);

        assertNotNull(createdOrder);
        assertEquals("PENDIENTE", createdOrder.getStatus());
    }
}