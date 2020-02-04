package com.github.tulliocba.bookstore.store.application.port.out;

import com.github.tulliocba.bookstore.store.domain.InventoryItem;

import java.util.Set;

public interface UpdateInventoryPort {

    void update(Set<InventoryItem> orderItems);
}
