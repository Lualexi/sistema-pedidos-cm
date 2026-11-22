package com.gestionpedidos.modules.warehouse.repository;

import com.gestionpedidos.modules.warehouse.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(String status);
}