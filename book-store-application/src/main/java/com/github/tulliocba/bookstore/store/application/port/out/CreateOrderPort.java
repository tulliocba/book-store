package com.github.tulliocba.bookstore.store.application.port.out;

import com.github.tulliocba.bookstore.store.domain.Order;

public interface CreateOrderPort {

    void save(Order order);

    Order loadById(Long id);
}
