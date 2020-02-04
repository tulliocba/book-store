package com.github.tulliocba.bookstore.store.application.port.out;

import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;

import java.util.Set;

public interface LoadInventoryPort {

    Set<InventoryItem> loadItemsById(Set<InventoryItemId> ids);
}
