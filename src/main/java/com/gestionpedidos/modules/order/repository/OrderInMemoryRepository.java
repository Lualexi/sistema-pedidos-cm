package com.gestionpedidos.modules.order.repository;

import com.gestionpedidos.modules.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderInMemoryRepository {

    private final Map<Long, Order> orderStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
        }
        orderStorage.put(order.getId(), order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orderStorage.get(id));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orderStorage.values());
    }
}