package com.gestionpedidos.modules.purchase.service.impl;

import com.gestionpedidos.modules.product.model.InventoryMovement;
import com.gestionpedidos.modules.product.model.Product;
import com.gestionpedidos.modules.product.repository.InventoryMovementRepository;
import com.gestionpedidos.modules.product.repository.ProductRepository;
import com.gestionpedidos.modules.purchase.model.PurchaseOrder;
import com.gestionpedidos.modules.purchase.model.PurchaseOrderItem;
import com.gestionpedidos.modules.purchase.model.Supplier;
import com.gestionpedidos.modules.purchase.repository.PurchaseOrderRepository;
import com.gestionpedidos.modules.purchase.repository.SupplierRepository;
import com.gestionpedidos.modules.purchase.service.PurchaseService;
import com.gestionpedidos.modules.user.model.User;
import com.gestionpedidos.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;
    private final UserRepository userRepository;

    public PurchaseServiceImpl(
            SupplierRepository supplierRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            ProductRepository productRepository,
            InventoryMovementRepository movementRepository,
            UserRepository userRepository
    ) {
        this.supplierRepository = supplierRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional
    public Supplier saveSupplier(Supplier supplier) {
        if (supplier.getName() == null || supplier.getName().isBlank()) {
            throw new RuntimeException("El nombre del proveedor es obligatorio.");
        }
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findAllPurchaseOrders() {
        return purchaseOrderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder, String creatorUsername) {
        if (purchaseOrder.getSupplier() == null || purchaseOrder.getSupplier().getId() == null) {
            throw new RuntimeException("Debe seleccionar un proveedor válido.");
        }
        Supplier supplier = supplierRepository.findById(purchaseOrder.getSupplier().getId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + purchaseOrder.getSupplier().getId()));

        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        if (purchaseOrder.getDocType() == null || purchaseOrder.getDocType().isBlank()) {
            purchaseOrder.setDocType("ORDEN_COMPRA");
        }
        if (purchaseOrder.getStatus() == null || purchaseOrder.getStatus().isBlank()) {
            purchaseOrder.setStatus("PENDIENTE");
        }
        if (purchaseOrder.getReferenceCode() == null || purchaseOrder.getReferenceCode().isBlank()) {
            purchaseOrder.setReferenceCode("OC-" + System.currentTimeMillis());
        }

        if (creatorUsername != null) {
            userRepository.findByUsername(creatorUsername).ifPresent(purchaseOrder::setCreatedBy);
        }

        BigDecimal total = BigDecimal.ZERO;
        if (purchaseOrder.getItems() != null && !purchaseOrder.getItems().isEmpty()) {
            for (PurchaseOrderItem item : purchaseOrder.getItems()) {
                if (item.getProduct() == null || item.getProduct().getId() == null) {
                    throw new RuntimeException("Cada ítem de compra debe referenciar un producto.");
                }
                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getProduct().getId()));
                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    throw new RuntimeException("La cantidad debe ser mayor a 0.");
                }
                BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : product.getPrice();
                item.setProduct(product);
                item.setUnitPrice(unitPrice);
                item.setPurchaseOrder(purchaseOrder);
                total = total.add(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        if (total.compareTo(BigDecimal.ZERO) > 0 || purchaseOrder.getTotalAmount() == null) {
            purchaseOrder.setTotalAmount(total);
        }

        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    @Transactional
    public PurchaseOrder receivePurchaseOrder(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con ID: " + id));

        if ("RECIBIDO".equalsIgnoreCase(po.getStatus())) {
            throw new RuntimeException("Esta orden ya fue recibida en almacén.");
        }

        for (PurchaseOrderItem item : po.getItems()) {
            Product product = item.getProduct();
            int newStock = product.getStock() + item.getQuantity();
            product.setStock(newStock);
            productRepository.save(product);

            movementRepository.save(InventoryMovement.builder()
                    .product(product)
                    .movementType("ENTRADA")
                    .quantity(item.getQuantity())
                    .stockAfter(newStock)
                    .reference("Compra " + po.getReferenceCode())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        po.setStatus("RECIBIDO");
        return purchaseOrderRepository.save(po);
    }
}