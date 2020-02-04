package com.github.tulliocba.controller;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutUseCase checkoutUseCase;

    @PostMapping
    void checkout() {

    }
}
