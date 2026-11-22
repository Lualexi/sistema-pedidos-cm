package com.gestionpedidos.modules.order.service.impl;

import com.gestionpedidos.modules.order.model.Order;
import com.gestionpedidos.modules.order.model.OrderItem;
import com.gestionpedidos.modules.order.repository.OrderRepository;
import com.gestionpedidos.modules.order.service.OrderService;
import com.gestionpedidos.modules.product.model.InventoryMovement;
import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.InventoryMovementRepository;
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
    private final InventoryMovementRepository movementRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            InventoryMovementRepository movementRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un producto.");
        }

        String orderType = (order.getOrderType() == null || order.getOrderType().isBlank())
                ? "ORDEN"
                : order.getOrderType().trim().toUpperCase();
        order.setOrderType(orderType);

        BigDecimal shipping = order.getShippingCost() != null ? order.getShippingCost() : BigDecimal.ZERO;
        order.setShippingCost(shipping);

        BigDecimal itemsTotal = BigDecimal.ZERO;
        order.setDate(LocalDateTime.now());

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("COTIZ".equals(orderType) ? "COTIZACION" : "PENDIENTE");
        }

        boolean deductStock = !"COTIZ".equals(orderType);

        for (OrderItem item : order.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new RuntimeException("Cada ítem debe tener un producto válido.");
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getProduct().getId()));

            if (deductStock) {
                if (product.getStock() < item.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + product.getName() + " (Stock actual: " + product.getStock() + ")");
                }
                int newStock = product.getStock() - item.getQuantity();
                product.setStock(newStock);
                productRepository.save(product);

                movementRepository.save(InventoryMovement.builder()
                        .product(product)
                        .movementType("SALIDA")
                        .quantity(item.getQuantity())
                        .stockAfter(newStock)
                        .reference("Pedido " + (order.getCustomerName() != null ? order.getCustomerName() : "General"))
                        .createdAt(LocalDateTime.now())
                        .build());
            }

            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setOrder(order);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            itemsTotal = itemsTotal.add(subtotal);
        }

        order.setTotal(itemsTotal.add(shipping));
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
        String nextStatus = status.trim().toUpperCase();

        if ("CANCELADO".equals(nextStatus) && !"CANCELADO".equalsIgnoreCase(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                int newStock = product.getStock() + item.getQuantity();
                product.setStock(newStock);
                productRepository.save(product);

                movementRepository.save(InventoryMovement.builder()
                        .product(product)
                        .movementType("REINTEGRO")
                        .quantity(item.getQuantity())
                        .stockAfter(newStock)
                        .reference("Cancelación pedido #" + order.getId())
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }

        order.setStatus(nextStatus);
        return orderRepository.save(order);
    }
}