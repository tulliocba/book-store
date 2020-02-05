package com.github.tulliocba.bookstore.store.application.port.in;

import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.application.service.PromotionCodeNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface CheckoutUseCase {

    void checkout(CheckoutCommand checkoutCommand) throws ItemUnavailableException, PromotionCodeNotFoundException;

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    class CheckoutCommand {

        @NotNull
        private final String customerId;
        @NotEmpty
        private final Set<Item> items;

        private final String promotionCode;

    }

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    class Item {
        @NotNull
        private final String itemId;

        @NotNull
        private final int quantity;
    }


}
