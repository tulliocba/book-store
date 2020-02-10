package com.github.tulliocba.bookstore.persistence.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {
    Set<InventoryItemEntity> findByIdIn(Set<Long> uuid);
}
