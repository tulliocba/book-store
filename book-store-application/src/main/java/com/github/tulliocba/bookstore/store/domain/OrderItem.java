package com.github.tulliocba.bookstore.store.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

@EqualsAndHashCode(of = "id")
@ToString
public class OrderItem {
    @Getter
    private OrderItemId id;
    @Getter
    private BigDecimal price;
    @Getter
    private int quantity;

    public static OrderItem with(final OrderItemId id, int quantity) {
        return new OrderItem(id, quantity);
    }

    public static OrderItem with(final OrderItemId id, BigDecimal price, int quantity) {
        return new OrderItem(id, price, quantity);
    }

    private OrderItem(OrderItemId id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    private OrderItem(OrderItemId id, BigDecimal price, int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    @Value
    public static class OrderItemId {
        private Long value;
    }

}
