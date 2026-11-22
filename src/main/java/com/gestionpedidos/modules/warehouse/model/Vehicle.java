package com.gestionpedidos.modules.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String plate;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "max_weight_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxWeightKg;

    @Column(name = "max_volume_m3", nullable = false, precision = 10, scale = 3)
    private BigDecimal maxVolumeM3;

    @Column(nullable = false, length = 25)
    @Builder.Default
    private String status = "DISPONIBLE";

    @Column(name = "available_from")
    private LocalDateTime availableFrom;
}