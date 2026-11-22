package com.gestionpedidos.modules.warehouse.service;

import com.gestionpedidos.modules.warehouse.model.RouteTariff;
import com.gestionpedidos.modules.warehouse.model.Vehicle;

import java.util.List;

public interface WarehouseService {
    List<Vehicle> findAllVehicles();
    Vehicle saveVehicle(Vehicle vehicle);
    Vehicle updateVehicleStatus(Long id, String status);
    void deleteVehicle(Long id);

    List<RouteTariff> findAllTariffs();
    RouteTariff saveTariff(RouteTariff tariff);
    void deleteTariff(Long id);
}