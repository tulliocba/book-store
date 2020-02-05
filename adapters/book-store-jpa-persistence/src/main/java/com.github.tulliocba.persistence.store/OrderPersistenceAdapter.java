package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderPersistenceAdapter implements CreateOrderPort {

    @Override
    public void save(Order order) {

    }
}
