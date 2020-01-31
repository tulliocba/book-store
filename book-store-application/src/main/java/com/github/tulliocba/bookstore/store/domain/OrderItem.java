package com.github.tulliocba.bookstore.store.domain;

import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;

public class OrderItem {
    @Getter private OrderItemId id;
    private int barcode;
    private BigDecimal price;
    private boolean reorder;
    private int reorderAmount;
    @Getter private int stock;

    public void decreaseStock(int quantity) {

    }

    @Value
    public static class OrderItemId {
        private String value;
    }
}
