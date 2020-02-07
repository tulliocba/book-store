package com.github.tulliocba.persistence.store;


import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.OrderItem.OrderItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@DataJpaTest
@Import({OrderPersistenceAdapter.class})
public class OrderPersistenceAdapterTest {

    @Autowired
    private OrderPersistenceAdapter adapter;

    @Test
    void should_save_a_order() {
        final Order order = new Order(getOrderItems(), new CustomerId(1L));

        adapter.save(order);

        final Long orderIdSaved = adapter.loadById(1L);

        Assertions.assertThat(order.getId().getValue()).isEqualTo(orderIdSaved);
    }

    private HashSet<OrderItem> getOrderItems() {
        return new HashSet<>(Arrays.asList(OrderItem.with(new OrderItemId(1L), new BigDecimal(9.90), 10)));
    }
}
