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
import java.util.Map;
import java.util.stream.Collectors;

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

    public void updateInventoryItems(List<Inventory> inventoryItems) {
        log.info("Inventory Items Updated!");
        inventoryRepository.saveAll(inventoryItems);
    }

    @Transactional
    public String updateStock(List<InventoryRequest> inventoryRequests) {
        Map<String, Integer> productQuantity = inventoryRequests.stream()
                .collect(Collectors.toMap(InventoryRequest::getProductId, InventoryRequest::getQuantity));
        log.info("keys :: {}", productQuantity.keySet());
        Map<String, Inventory> inventoryMap = inventoryRepository.findByProductIdIn(productQuantity.keySet()).stream()
                .collect(Collectors.toMap(Inventory::getProductId, inventory -> inventory));

        List<Inventory> inventoryList = null;
        for (String productId : productQuantity.keySet()) {
            Inventory inventory = inventoryMap.get(productId);
            inventory.setQuantity(inventory.getQuantity() - productQuantity.get(productId));
            inventoryList.add(inventory);
        }
        inventoryRepository.saveAll(inventoryList);

        return "Inventory Updated";
    }

    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream().map(this::mapToInventoryResponse).toList();
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .build();
    }
}