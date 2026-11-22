package com.gestionpedidos.modules.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 80)
    private String category;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String currency = "PEN";

    @Column(name = "min_stock", nullable = false)
    @Builder.Default
    private Integer minStock = 10;

    @Column(name = "unit_volume_m3", precision = 10, scale = 3)
    private BigDecimal unitVolumeM3;

    @Column(name = "unit_weight_kg", precision = 10, scale = 2)
    private BigDecimal unitWeightKg;
}