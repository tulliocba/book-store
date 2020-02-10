package com.github.tulliocba.bookstore.persistence.store;


import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.OrderItem.OrderItemId;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({OrderPersistenceAdapter.class})
public class OrderPersistenceAdapterTest {

    @Autowired
    private OrderPersistenceAdapter adapter;

    @Test
    @Sql({"CreateInventory.sql", "CreatePromotion.sql", "CreateUser.sql"})
    void should_save_a_order() {
        final Order order = new Order(getOrderItems(), new CustomerId(1L));

        order.applyPromotion(new Promotion("CODE_TEST", 10,
                LocalDateTime.of(LocalDate.now().plusYears(1), LocalTime.now())));

        adapter.save(order);

        final Order orderSaved = adapter.loadById(1L);

        assertThat(orderSaved).isNotNull();
        assertThat(orderSaved.getCustomerId().getValue()).isEqualTo(1L);
        assertThat(orderSaved.getPromotion().getCode()).isEqualTo("CODE_TEST");
        assertThat(orderSaved.getOrderItems().size()).isEqualTo(1);
    }

    private HashSet<OrderItem> getOrderItems() {
        return new HashSet<>(Arrays.asList(OrderItem.with(new OrderItemId(1L), new BigDecimal(9.90), 10)));
    }
}
