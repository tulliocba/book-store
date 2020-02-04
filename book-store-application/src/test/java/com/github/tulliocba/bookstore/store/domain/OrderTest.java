package com.github.tulliocba.bookstore.store.domain;

import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    private Set<OrderItem> items;

    @Before
    public void setUp() {
        final OrderItem orderItem1 = OrderItem.with(new OrderItem.OrderItemId(randomUUID().toString()), new BigDecimal(25), 6);
        final OrderItem orderItem2 = OrderItem.with(new OrderItem.OrderItemId(randomUUID().toString()), new BigDecimal(50), 3);
        final OrderItem orderItem3 = OrderItem.with(new OrderItem.OrderItemId(randomUUID().toString()), new BigDecimal(75), 2);

        items = new HashSet<>(Arrays.asList(orderItem1, orderItem2, orderItem3));
    }

    @Test
    public void should_create_a_new_order_without_promotion_code() {
        final Order order = new Order(items, new CustomerId(randomUUID().toString()));

        assertThat(order.getOrderItems().size()).isEqualTo(3);
        assertThat(order.getTotal()).isEqualTo(new BigDecimal(450));
    }

    @Test
   public void should_create_a_new_order_with_promotion_code() {
        final Order order = new Order(items, new CustomerId(randomUUID().toString()));

        order.applyPromotion(new Promotion(randomUUID().toString(), 10,
                LocalDateTime.now().plusDays(1)));

        assertThat(order.getTotal()).isEqualTo(new BigDecimal(405));

    }

    @Test(expected = PromotionAppliedException.class)
   public void should_not_apply_expired_promotion_code() {
        final Order order = new Order(items, new CustomerId(randomUUID().toString()));

        order.applyPromotion(new Promotion(randomUUID().toString(), 10, LocalDateTime.now().minusDays(1)));
    }

    @Test(expected = PromotionAppliedException.class)
    public void should_not_apply_a_second_promotion_code() {
        final Order order = new Order(items, new CustomerId(randomUUID().toString()));

        order.applyPromotion(new Promotion(randomUUID().toString(), 10, LocalDateTime.now().plusDays(1)));
        order.applyPromotion(new Promotion(randomUUID().toString(), 10, LocalDateTime.now().plusDays(1)));
    }
}
