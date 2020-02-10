package com.github.tulliocba.bookstore.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.LoadInventoryPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Repository
public class InventoryPersistenceAdapter implements UpdateInventoryPort, LoadInventoryPort {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public Set<InventoryItem> loadItemsById(Set<InventoryItemId> ids) {

        final Set<InventoryItemEntity> inventoryItems = inventoryItemRepository.findByIdIn(getIds(ids));
        return inventoryItems.stream().map(item ->
                new InventoryItem(new InventoryItemId(item.getId()),
                        item.getPrice(),
                        item.getStock())).collect(Collectors.toSet());
    }

    private Set<Long> getIds(Set<InventoryItemId> ids) {
        return ids.stream().map(id -> id.getValue()).collect(Collectors.toSet());
    }

    @Override
    public void update(Set<InventoryItem> orderItems) {
        final Set<InventoryItemEntity> items = orderItems
                .stream()
                .map(item -> InventoryItemEntity.toEntity(item)).collect(Collectors.toSet());

        for (InventoryItemEntity item : items) {
            inventoryItemRepository.save(item);
        }
    }
}
