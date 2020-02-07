package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    @ManyToMany
    @JoinTable(
            name = "order_order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id"))
    private Set<InventoryItemEntity> items;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;

    public static OrderEntity toEntity(Order order) {
        // TODO
        return null;
    }
}
