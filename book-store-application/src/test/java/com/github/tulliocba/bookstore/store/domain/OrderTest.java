package com.github.tulliocba.bookstore.store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    private Set<OrderItem> items;

    @BeforeEach
    void setUp() {
        final OrderItem orderItem1 = OrderItem.with(new OrderItem.OrderItemId(randomUUID().toString()), new BigDecimal(25), 6);
        final OrderItem orderItem2 = OrderItem.with(new OrderItem.OrderItemId(randomUUID().toString()), new BigDecimal(50), 3);
        final OrderItem orderItem3 = OrderItem.with(new OrderItem.OrderItemId(randomUUID().toString()), new BigDecimal(75), 2);

        items = new HashSet<>(Arrays.asList(orderItem1, orderItem2, orderItem3));
    }

    @Test
    void should_create_a_new_order_without_promotion_code() {
        final Order order = Order.withItems(items);

        assertThat(order.getOrderItems().size()).isEqualTo(3);
        assertThat(order.getTotal()).isEqualTo(new BigDecimal(450));
    }

    @Test
    void should_create_a_new_order_with_promotion_code() {
        final Order order = Order.withItems(items);

        order.applyPromotion(new Promotion(randomUUID().toString(), 10,
                LocalDateTime.of(2020, 02, 04, 00, 00, 00)));

        assertThat(order.getTotal()).isEqualTo(new BigDecimal(405));

    }

    @Test
    void should_not_apply_expired_promotion_code() {
        final Order order = Order.withItems(items);

        assertThrows(PromotionAppliedException.class, () -> {

            order.applyPromotion(new Promotion(randomUUID().toString(), 10, LocalDateTime.now().minusDays(1)));
        }, "Should not apply a expired promotion code");
    }

    @Test
    void should_not_apply_a_second_promotion_code() {
        final Order order = Order.withItems(items);

        assertThrows(PromotionAppliedException.class, () -> {
            order.applyPromotion(new Promotion(randomUUID().toString(), 10, LocalDateTime.now().plusDays(1)));
            order.applyPromotion(new Promotion(randomUUID().toString(), 10, LocalDateTime.now().plusDays(1)));
        }, "Should not apply a second promotion code");
    }
}
