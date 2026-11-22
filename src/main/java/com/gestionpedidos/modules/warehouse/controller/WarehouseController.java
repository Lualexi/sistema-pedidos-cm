package com.gestionpedidos.modules.warehouse.controller;

import com.gestionpedidos.modules.warehouse.model.RouteTariff;
import com.gestionpedidos.modules.warehouse.model.Vehicle;
import com.gestionpedidos.modules.warehouse.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getVehicles() {
        return ResponseEntity.ok(warehouseService.findAllVehicles());
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.saveVehicle(vehicle));
    }

    @PatchMapping("/vehicles/{id}/status")
    public ResponseEntity<Vehicle> updateVehicleStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(warehouseService.updateVehicleStatus(id, body.get("status")));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        warehouseService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tariffs")
    public ResponseEntity<List<RouteTariff>> getTariffs() {
        return ResponseEntity.ok(warehouseService.findAllTariffs());
    }

    @PostMapping("/tariffs")
    public ResponseEntity<RouteTariff> createTariff(@RequestBody RouteTariff tariff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.saveTariff(tariff));
    }

    @DeleteMapping("/tariffs/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        warehouseService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }
}