package com.gestionpedidos.modules.order.service;

import com.gestionpedidos.modules.order.model.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> findAll();
    Order findById(Long id);
    Order updateStatus(Long id, String status);
}