package com.gestionpedidos.modules.purchase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(length = 120)
    private String contact;

    @Column(length = 40)
    private String phone;

    @Column(length = 120)
    private String email;
}