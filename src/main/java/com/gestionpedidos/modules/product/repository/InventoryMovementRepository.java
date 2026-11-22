package com.gestionpedidos.modules.product.repository;

import com.gestionpedidos.modules.product.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findTop100ByOrderByCreatedAtDesc();
    List<InventoryMovement> findByProductIdOrderByCreatedAtDesc(Long productId);
}