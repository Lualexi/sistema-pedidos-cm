package com.gestionpedidos.modules.order.service.impl;

import com.gestionpedidos.modules.order.model.Order;
import com.gestionpedidos.modules.order.model.OrderItem;
import com.gestionpedidos.modules.order.repository.OrderInMemoryRepository;
import com.gestionpedidos.modules.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderInMemoryRepository orderRepository;

    public OrderServiceImpl(OrderInMemoryRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDIENTE");
        
        // Calcular el monto total sumando los subtotales de los ítems
        double total = 0.0;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                if (item.getSubtotal() == null) {
                    item.setSubtotal(item.getQuantity() * item.getUnitPrice());
                }
                total += item.getSubtotal();
            }
        }
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con el ID: " + id));
    }
}