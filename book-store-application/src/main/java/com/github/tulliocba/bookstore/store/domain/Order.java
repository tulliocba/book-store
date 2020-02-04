package com.github.tulliocba.bookstore.store.domain;

import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
public class Order {

    @Getter
    private OrderId id;
    @Getter
    private CustomerId customerId;
    @Getter
    private Set<OrderItem> orderItems;
    @Getter
    private BigDecimal total = BigDecimal.ZERO;

    private Promotion promotion;


    public Order(Set<OrderItem> orderItems, CustomerId customerId) {
        this.orderItems = orderItems;
        this.customerId = customerId;
        performCalculationOfTotal(orderItems);
    }

    private void performCalculationOfTotal(Set<OrderItem> orderItems) {
        for(OrderItem item : orderItems) {
            total = total.add(getTotalByItem(item));
        }
    }

    private BigDecimal getTotalByItem(OrderItem item) {
        return item.getPrice().multiply(new BigDecimal(item.getQuantity()));
    }


    public boolean isPromotionAdded() {
        return promotion != null;
    }

    public void applyPromotion(Promotion promotion) {

        setPromotionCode(promotion);

        if(LocalDateTime.now().isAfter(this.promotion.getExpiration())) throw new InvalidPromotionCodeException("The promotion code has expired");

        total = total.subtract(new BigDecimal(total.doubleValue() * (this.promotion.getPercentage() / Double.valueOf(100))));
    }

    private void setPromotionCode(Promotion promotion) {
        if(isPromotionAdded()) throw new InvalidPromotionCodeException("The order has already applied a promotion code");

        this.promotion = promotion;
    }

    @Value
    public static class OrderId {
        private String value;
    }
}
