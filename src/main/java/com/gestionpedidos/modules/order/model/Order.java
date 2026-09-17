package com.gestionpedidos.modules.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String customerName;
    private LocalDateTime orderDate;
    private String status; // PENDIENTE, COMPLETADO, CANCELADO
    private Double totalAmount;
    private List<OrderItem> items = new ArrayList<>();
}