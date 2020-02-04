package com.github.tulliocba.bookstore.store.application.port.out;

import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.domain.OrderItem;

import java.util.Set;

public interface UpdateInventoryPort {

    Set<OrderItem> decrementInventory(Set<OrderItem> orderItems) throws ItemUnavailableException;
}
