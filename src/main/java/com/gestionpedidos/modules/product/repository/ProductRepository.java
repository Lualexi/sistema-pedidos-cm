package com.gestionpedidos.modules.product.repository;

import com.gestionpedidos.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}