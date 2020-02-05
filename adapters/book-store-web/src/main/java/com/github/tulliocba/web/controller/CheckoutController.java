package com.github.tulliocba.web.controller;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.CheckoutCommand;
import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.application.service.PromotionCodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutUseCase checkoutUseCase;

    @PostMapping("/book-store/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    void checkout(@RequestBody @Valid CheckoutCommand command) throws ItemUnavailableException, PromotionCodeNotFoundException {
        checkoutUseCase.checkout(command);
    }
}
