package com.adiths.inventoryservice.controller;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.dto.InventoryResponse;
import com.adiths.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @PostMapping
    public ResponseEntity<String> createInventory(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.createInventory(inventoryRequest);
    }

    @PutMapping("/stock")
    public ResponseEntity<String> updateStock(@RequestBody List<InventoryRequest> inventoryRequests) {
        return inventoryService.updateStock(inventoryRequests);
    }
}