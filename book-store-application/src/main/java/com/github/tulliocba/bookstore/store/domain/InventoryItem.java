package com.github.tulliocba.bookstore.store.domain;

import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

@EqualsAndHashCode(of = "id")
@ToString
public class InventoryItem {

    @Getter
    private InventoryItemId id;
    @Getter
    private BigDecimal price;
    @Getter
    private int stock;

    public InventoryItem(InventoryItemId id, BigDecimal price, int stock) {
        this.id = id;
        this.price = price;
        this.stock = stock;
    }

    @Value
    public static class InventoryItemId {
        public String value;
    }

    public InventoryItem decrementStock(int quantity) throws ItemUnavailableException {
        if(quantity <= 0) throw new IllegalArgumentException("The quantity cannot be negative or less then one");

        if(quantity > this.stock) {
            throw new ItemUnavailableException("The item of id: "+ this.id.getValue() +" does not have enough stock");
        }

        this.stock -= quantity;

        return this;
    }
}
