package com.gestionpedidos.modules.purchase.repository;

import com.gestionpedidos.modules.purchase.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}