package com.github.tulliocba.bookstore.store.application.port.in;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface CheckoutUseCase {

    void checkout(CheckoutCommand checkoutCommand);

    @Value
    class CheckoutCommand {

        @NotNull
        private final Long customerId;
        @NotEmpty
        private final Set<OrderItem> items;

        private final int promotionCode;

    }

    @Value
    class OrderItem {
        @NotNull
        private final Long itemId;

        @NotNull
        private final Long quantity;
    }


}
