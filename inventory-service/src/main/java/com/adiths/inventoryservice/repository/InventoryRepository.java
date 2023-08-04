package com.adiths.inventoryservice.repository;

import com.adiths.inventoryservice.model.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Set;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findByProductIdIn(Set<String> productId);
}