package com.gestionpedidos.modules.warehouse.repository;

import com.gestionpedidos.modules.warehouse.model.RouteTariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteTariffRepository extends JpaRepository<RouteTariff, Long> {
    List<RouteTariff> findByDestinationContainingIgnoreCase(String destination);
}