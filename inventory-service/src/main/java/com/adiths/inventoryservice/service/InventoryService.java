package com.adiths.inventoryservice.service;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.dto.InventoryResponse;
import com.adiths.inventoryservice.model.Inventory;
import com.adiths.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setProductId(inventoryRequest.getProductId());
        inventoryRepository.save(inventory);

        log.info("Inventory {} is saved", inventory.getId());
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<Long> productId) {
        return inventoryRepository.findByProductIdIn(productId).stream()
                .map(this::mapToInventoryResponse).toList();
    }

    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream().map(this::mapToInventoryResponse).toList();
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .build();
    }
}