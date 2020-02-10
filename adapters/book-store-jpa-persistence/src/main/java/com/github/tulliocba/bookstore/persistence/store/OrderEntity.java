package com.github.tulliocba.bookstore.persistence.store;

import com.github.tulliocba.bookstore.store.domain.Order;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"items"})
public class OrderEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemEntity> items;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;

    public OrderEntity(Order order, Optional<PromotionEntity> promotion, Optional<UserEntity> customer) {
        if(promotion.isPresent()) this.promotion = promotion.get();
        this.customer = customer.orElseThrow(() -> new IllegalArgumentException("The customer is mandatory"));
        this.total = order.getTotal();
        this.items = getOrderItems(order);
    }

    private Set<OrderItemEntity> getOrderItems(Order order) {
        return order.getOrderItems()
            .stream()
                .map(item ->
                    new OrderItemEntity(this, new InventoryItemEntity(item.getId().getValue()),
                            item.getQuantity())).collect(Collectors.toSet());
    }
}
