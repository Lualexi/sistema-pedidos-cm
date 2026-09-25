package com.gestionpedidos.modules.order.service.impl;

import com.gestionpedidos.modules.order.model.Order;
import com.gestionpedidos.modules.order.model.OrderItem;
import com.gestionpedidos.modules.order.repository.OrderRepository;
import com.gestionpedidos.modules.order.service.OrderService;
import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un producto.");
        }

        BigDecimal total = BigDecimal.ZERO;
        order.setDate(LocalDateTime.now());

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("PENDIENTE");
        }

        for (OrderItem item : order.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new RuntimeException("Cada ítem debe tener un producto válido.");
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getProduct().getId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getName() + " (Stock actual: " + product.getStock() + ")");
            }

            // Actualizar stock en base de datos
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setOrder(order);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Order updateStatus(Long id, String status) {
        Order order = findById(id);

        // Si se cancela el pedido, devolvemos el stock al inventario
        if ("CANCELADO".equalsIgnoreCase(status) && !"CANCELADO".equalsIgnoreCase(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(status.toUpperCase());
        return orderRepository.save(order);
    }
}