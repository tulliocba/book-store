package com.github.tulliocba.bookstore.store.application.port.in;

import com.github.tulliocba.bookstore.store.application.service.InvalidPromotionException;
import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

public interface CheckoutUseCase {

    void checkout(CheckoutCommand checkoutCommand) throws ItemUnavailableException, InvalidPromotionException;

    @Value
    class CheckoutCommand {

        @NotNull
        private final String customerId;
        @NotEmpty
        private final Set<Item> items;

        private final String promotionCode;

        public Set<OrderItem> toOrderItem() {
            return this.items
                    .stream()
                    .map(item -> OrderItem.with(new OrderItem.OrderItemId(item.getItemId()), item.getQuantity()))
                    .collect(Collectors.toSet());
        }

    }

    @Value
    class Item {
        @NotNull
        private final String itemId;

        @NotNull
        private final int quantity;
    }


}
