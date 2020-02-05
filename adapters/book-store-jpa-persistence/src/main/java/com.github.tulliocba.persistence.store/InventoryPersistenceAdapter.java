package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.LoadInventoryPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class InventoryPersistenceAdapter implements UpdateInventoryPort, LoadInventoryPort {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public Set<InventoryItem> loadItemsById(Set<InventoryItemId> ids) {

        final Set<InventoryItemEntity> inventoryItems = inventoryItemRepository.findByUuidContains(getIds(ids));

        return inventoryItems.stream().map(item ->
                new InventoryItem(new InventoryItemId(item.getUuid()),
                        item.getPrice(),
                        item.getStock())).collect(Collectors.toSet());
    }

    private Set<String> getIds(Set<InventoryItemId> ids) {
        return ids.stream().map(id -> id.getValue()).collect(Collectors.toSet());
    }

    @Override
    public void update(Set<InventoryItem> orderItems) {
        final Set<InventoryItemEntity> items = orderItems
                .stream()
                .map(item -> InventoryItemEntity.toEntity(item)).collect(Collectors.toSet());

        for (InventoryItemEntity item : items) inventoryItemRepository.save(item);
    }
}
