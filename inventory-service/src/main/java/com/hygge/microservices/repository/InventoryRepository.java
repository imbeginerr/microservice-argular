package com.hygge.microservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hygge.microservices.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, int quantity);
}
