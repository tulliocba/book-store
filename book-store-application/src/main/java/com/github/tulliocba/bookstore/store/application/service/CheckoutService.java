package com.github.tulliocba.bookstore.store.application.service;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {


    @Override
    public void checkout(final CheckoutCommand checkoutCommand) {

    }
}

