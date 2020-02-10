package com.github.tulliocba.bookstore.store.application.port.out;

import com.github.tulliocba.bookstore.store.domain.Order;

public interface LoadOrderPort {
    Order loadById(Long id);
}
