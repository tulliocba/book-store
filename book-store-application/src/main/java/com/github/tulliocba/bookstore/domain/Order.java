package com.github.tulliocba.bookstore.domain;

import java.util.Set;

public class Order {
    private boolean promotionAdded;
    private Promotion promotion;
    private Set<OrderItem> orderItems;
}
