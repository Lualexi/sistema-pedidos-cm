package com.gestionpedidos.modules.warehouse.service.impl;

import com.gestionpedidos.modules.warehouse.model.RouteTariff;
import com.gestionpedidos.modules.warehouse.model.Vehicle;
import com.gestionpedidos.modules.warehouse.repository.RouteTariffRepository;
import com.gestionpedidos.modules.warehouse.repository.VehicleRepository;
import com.gestionpedidos.modules.warehouse.service.WarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final VehicleRepository vehicleRepository;
    private final RouteTariffRepository routeTariffRepository;

    public WarehouseServiceImpl(VehicleRepository vehicleRepository, RouteTariffRepository routeTariffRepository) {
        this.vehicleRepository = vehicleRepository;
        this.routeTariffRepository = routeTariffRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    @Transactional
    public Vehicle saveVehicle(Vehicle vehicle) {
        if (vehicle.getPlate() == null || vehicle.getPlate().isBlank()) {
            throw new RuntimeException("La placa del vehículo es obligatoria.");
        }
        if (vehicle.getMaxWeightKg() == null || vehicle.getMaxWeightKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("La capacidad de peso debe ser mayor a 0.");
        }
        if (vehicle.getMaxVolumeM3() == null || vehicle.getMaxVolumeM3().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("La capacidad de volumen debe ser mayor a 0.");
        }
        if (vehicle.getStatus() == null || vehicle.getStatus().isBlank()) {
            vehicle.setStatus("DISPONIBLE");
        }
        vehicle.setPlate(vehicle.getPlate().trim().toUpperCase());
        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public Vehicle updateVehicleStatus(Long id, String status) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
        vehicle.setStatus(status.toUpperCase());
        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con ID: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteTariff> findAllTariffs() {
        return routeTariffRepository.findAll();
    }

    @Override
    @Transactional
    public RouteTariff saveTariff(RouteTariff tariff) {
        if (tariff.getOrigin() == null || tariff.getOrigin().isBlank()
                || tariff.getDestination() == null || tariff.getDestination().isBlank()) {
            throw new RuntimeException("El origen y destino del trayecto son obligatorios.");
        }
        if (tariff.getRoundTripCost() == null || tariff.getRoundTripCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El costo de ida y vuelta no puede ser negativo.");
        }
        if (tariff.getEstimatedHours() == null || tariff.getEstimatedHours().compareTo(BigDecimal.ZERO) <= 0) {
            tariff.setEstimatedHours(BigDecimal.valueOf(2.00));
        }
        return routeTariffRepository.save(tariff);
    }

    @Override
    @Transactional
    public void deleteTariff(Long id) {
        if (!routeTariffRepository.existsById(id)) {
            throw new RuntimeException("Tarifa no encontrada con ID: " + id);
        }
        routeTariffRepository.deleteById(id);
    }
}