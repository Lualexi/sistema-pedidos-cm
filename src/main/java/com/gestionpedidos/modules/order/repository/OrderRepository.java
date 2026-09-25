package com.gestionpedidos.modules.order.repository;

import com.gestionpedidos.modules.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}