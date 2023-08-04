package com.adiths.inventoryservice.controller;

import com.adiths.inventoryservice.dto.InventoryRequest;
import com.adiths.inventoryservice.dto.InventoryResponse;
import com.adiths.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInventory(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.createInventory(inventoryRequest);
    }

    @PutMapping("/stock")
    @ResponseStatus(HttpStatus.OK)
    public String updateStock(@RequestBody List<InventoryRequest> inventoryRequests) {
        return inventoryService.updateStock(inventoryRequests);
    }
}