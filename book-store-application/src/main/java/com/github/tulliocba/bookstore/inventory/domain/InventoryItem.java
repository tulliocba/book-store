package com.github.tulliocba.bookstore.inventory.domain;

import lombok.Value;

import java.math.BigDecimal;

public class InventoryItem {
    private InventoryItemId id;
    private BigDecimal price;
    private int stock;


    @Value
    public static class InventoryItemId {
        private String value;
    }
}
