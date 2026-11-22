package com.gestionpedidos.modules.purchase.service;

import com.gestionpedidos.modules.purchase.model.PurchaseOrder;
import com.gestionpedidos.modules.purchase.model.Supplier;

import java.util.List;

public interface PurchaseService {
    List<Supplier> findAllSuppliers();
    Supplier saveSupplier(Supplier supplier);
    void deleteSupplier(Long id);

    List<PurchaseOrder> findAllPurchaseOrders();
    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder, String creatorUsername);
    PurchaseOrder receivePurchaseOrder(Long id);
}