package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.LoadInventoryPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;


@RequiredArgsConstructor
@Component
public class InventoryPersistenceAdapter implements UpdateInventoryPort, LoadInventoryPort {


    @Override
    public Set<InventoryItem> loadItemsById(Set<InventoryItem.InventoryItemId> ids) {
        return null;
    }

    @Override
    public void update(Set<InventoryItem> orderItems) {

    }
}
