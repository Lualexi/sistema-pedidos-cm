package com.gestionpedidos.modules.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "route_tariffs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String origin;

    @Column(nullable = false, length = 120)
    private String destination;

    @Column(name = "round_trip_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal roundTripCost;

    @Column(name = "estimated_hours", nullable = false, precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal estimatedHours = BigDecimal.valueOf(2.00);
}