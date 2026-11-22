package com.gestionpedidos.modules.purchase.controller;

import com.gestionpedidos.modules.purchase.model.PurchaseOrder;
import com.gestionpedidos.modules.purchase.model.Supplier;
import com.gestionpedidos.modules.purchase.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getSuppliers() {
        return ResponseEntity.ok(purchaseService.findAllSuppliers());
    }

    @PostMapping("/suppliers")
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.saveSupplier(supplier));
    }

    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        purchaseService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrders() {
        return ResponseEntity.ok(purchaseService.findAllPurchaseOrders());
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(
            @RequestBody PurchaseOrder purchaseOrder,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseService.createPurchaseOrder(purchaseOrder, username));
    }

    @PatchMapping("/orders/{id}/receive")
    public ResponseEntity<PurchaseOrder> receivePurchaseOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.receivePurchaseOrder(id));
    }
}