package com.github.tulliocba.bookstore.store.domain;

import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventoryItemTest {

    private Set<InventoryItem> items;

    private static int DEFAULT_STOCK = 3;

    @Before
    public void setUp() throws Exception {
        this.items = new HashSet<>();
        items.add(new InventoryItem(new InventoryItemId(1L), new BigDecimal(10), DEFAULT_STOCK));
        items.add(new InventoryItem(new InventoryItemId(2L), new BigDecimal(20), DEFAULT_STOCK));
        items.add(new InventoryItem(new InventoryItemId(3L), new BigDecimal(30), DEFAULT_STOCK));
    }

    @Test
    public void should_succeeds_when_decrement_inventory_item_stock() throws ItemUnavailableException {
        for (InventoryItem item : items) {
            item.decrementStock(1);
            assertThat(item.getStock()).isEqualTo(DEFAULT_STOCK - 1);
        }
    }

    @Test
    public void should_fail_when_quantity_is_negative() {
        for (InventoryItem item : items) {
            assertThrows(IllegalArgumentException.class, () -> item.decrementStock(-1));
        }
    }

    @Test
    public void should_fail_when_quantity_is_greater_than_the_available_stock() throws ItemUnavailableException {
        for (InventoryItem item : items) {
            assertThrows(ItemUnavailableException.class, () -> item.decrementStock(5));
        }
    }
}
