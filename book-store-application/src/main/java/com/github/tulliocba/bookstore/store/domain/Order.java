package com.github.tulliocba.bookstore.store.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
public class Order {

    private OrderId id;

    private Promotion promotion;
    @Getter
    private Set<OrderItem> orderItems;
    @Getter
    private BigDecimal total = BigDecimal.ZERO;


    private Order(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
        performCalculationOfTotal(orderItems);
    }

    private void performCalculationOfTotal(Set<OrderItem> orderItems) {
        for(OrderItem item : orderItems) {
            total = total.add(item.getPrice());
        }
    }

    public static Order withItems(Set<OrderItem> orderItems) {
        return new Order(orderItems);
    }

    public boolean isPromotionAdded() {
        return promotion != null;
    }

    public void applyPromotion(Promotion promotion) {
        if(isPromotionAdded()) throw new PromotionAppliedException("The order has already applied a promotion code");

        total = total.subtract(new BigDecimal(total.doubleValue() * (promotion.getPercentage() / 100)));
    }

    @Value
    public static class OrderId {
        private String value;
    }
}
