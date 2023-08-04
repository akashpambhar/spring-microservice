package com.adiths.inventoryservice.service;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.dto.InventoryResponse;
import com.adiths.inventoryservice.model.Inventory;
import com.adiths.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public ResponseEntity<String> createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setProductId(inventoryRequest.getProductId());
        inventoryRepository.save(inventory);

        return new ResponseEntity<>("Inventory Created", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateStock(List<InventoryRequest> inventoryRequests) {
        for (InventoryRequest inventoryRequest : inventoryRequests) {
            Inventory inventory = inventoryRepository.findByProductId(inventoryRequest.getProductId());
            inventory.setQuantity(inventory.getQuantity() - inventoryRequest.getQuantity());
        }

        return new ResponseEntity<>("Inventory Updated", HttpStatus.OK);
    }

    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return new ResponseEntity<>(inventoryRepository.findAll().stream().map(this::mapToInventoryResponse).toList(), HttpStatus.OK);
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .build();
    }
}