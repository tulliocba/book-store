package com.github.tulliocba.persistence.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {

    Set<InventoryItemEntity> findByUuidContains(Set<String> uuidSet);
}
