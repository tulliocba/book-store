package com.github.tulliocba.bookstore.application.service;

import com.github.tulliocba.bookstore.application.port.in.CheckoutUseCase;
import com.github.tulliocba.bookstore.application.port.out.LoadItemsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    private LoadItemsPort loadItemsPort;

    @Override
    public void checkout(final CheckoutCommand checkoutCommand) {

        final Set<com.github.tulliocba.bookstore.domain.OrderItem.OrderItemId> itemIds = checkoutCommand.getItems().stream()
                .map(item -> new com.github.tulliocba.bookstore.domain.OrderItem.OrderItemId(item.getItemId().toString()))
                .collect(Collectors.toSet());

        final Set<com.github.tulliocba.bookstore.domain.OrderItem> items = loadItemsPort.byIds(itemIds);

        if(itemIds.size() != items.size()) throw new RuntimeException("There is one or more items not registered");


    }
}

